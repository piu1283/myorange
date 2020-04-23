package com.ood.myorange.service.impl;

import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.constant.PermissionConstant;
import com.ood.myorange.constant.enumeration.Gender;
import com.ood.myorange.dao.UserDao;
import com.ood.myorange.dto.AdminUserDto;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.dto.UserProfile;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.pojo.Permission;
import com.ood.myorange.pojo.User;
import com.ood.myorange.service.*;
import com.ood.myorange.util.ModelMapperUtil;
import com.ood.myorange.util.PasswordUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Chen on 2/24/20.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    UserPermissionService userPermissionService;

    @Autowired
    StorageConfigService storageConfigService;

    @Autowired
    DirService dirService;

    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    ICurrentAccount currentAccount;

    @Override
    public User getUserById(int userId) {
        return userDao.selectByPrimaryKey(new User(userId));
    }

    @Override
    public void increaseUsedSize(int userId, Long size) {
        userDao.updateUsedSize(userId,size);
    }

    @Override
    public List<AdminUserDto> getAllAdminUser() {
        // get all user
        List<User> users = userDao.getUsers();
        // get all permission
        Map<Integer, List<String>> permissionMap = userPermissionService.getAllPermission();
        // get all source
        List<StorageConfigDto> configDtos = storageConfigService.getAllConfigurationsWithoutDetail();
        Map<Integer, StorageConfigDto> configDtoMap = configDtos.stream().collect(Collectors.toMap(StorageConfigDto::getId, storageConfigDto -> storageConfigDto));
        List<AdminUserDto> res = new ArrayList<>();
        // 2. User -> UserRequest(UserDto)
        for (User user : users) {
            // add other logic
            List<String> permissionList = permissionMap.get(user.getId());
            StorageConfigDto configDto = configDtoMap.get(user.getId());
            res.add(mapUserToAdminUserDto(user, permissionList, configDto));
        }
        return res;
    }

    @Override
    public AdminUserDto getAdminUserByUserId(int userId) {
        // get user
        User user = userDao.selectByPrimaryKey(new User(userId));
        if (user == null) {
            throw new ResourceNotFoundException("cannot found user. id: " + userId);
        }
        // get permission
        List<String> permissionList = userPermissionService.getPermissionListByUserId(userId)
                .stream()
                .map(Permission::getPermissionName)
                .collect(Collectors.toList());
        // get all source
        StorageConfigDto configDto = storageConfigService.getConfigurationsWithoutDetail(user.getSourceId());
        // add other logic
        return mapUserToAdminUserDto(user, permissionList, configDto);
    }

    @Override
    @Transactional
    public void modifyUserPermissionAndMemory(AdminUserDto adminUserDto) {
        User user = userDao.selectByPrimaryKey(new User(adminUserDto.getId()));
        if (user == null) {
            throw new ResourceNotFoundException("Cannot found user, id: " + adminUserDto.getId());
        }
        // change permission
        List<String> obtainPermissionList = new ArrayList<>();
        List<String> removePermissionList = new ArrayList<>();
        if (adminUserDto.getDownloadAccess() != null) {
            if (adminUserDto.getDownloadAccess())
                obtainPermissionList.add(PermissionConstant.DOWNLOAD.toString());
            else
                removePermissionList.add(PermissionConstant.DOWNLOAD.toString());
        }
        if (adminUserDto.getUploadAccess() != null) {
            if (adminUserDto.getUploadAccess())
                obtainPermissionList.add(PermissionConstant.UPLOAD.toString());
            else
                removePermissionList.add(PermissionConstant.UPLOAD.toString());
        }
        userPermissionService.changePermission(adminUserDto.getId(), obtainPermissionList, removePermissionList);
        // change memory and block
        boolean needUpdate = false;
        if (adminUserDto.getTotalStorage() != null) {
            user.setMemorySize(adminUserDto.getTotalStorage());
            needUpdate = true;
        }
        if (adminUserDto.getBlockedStatus() != null) {
            user.setBlocked(adminUserDto.getBlockedStatus());
            needUpdate = true;
        }
        if (needUpdate) {
            userDao.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    @Transactional
    public void addUser(AdminUserDto adminUserDto) {
        // generate password
        String pass = RandomStringUtils.random(6, true, true);
        String passAfterEncode = PasswordUtil.encodePassword(pass);
        // check email
        User user  = userDao.getUserByEmail(adminUserDto.getEmail());
        if (user != null) {
            throw new InvalidRequestException("User exist for email: " + adminUserDto.getEmail());
        }
        user = new User();
        user.setMemorySize(adminUserDto.getTotalStorage());
        user.setFirstName(adminUserDto.getFirstName());
        user.setLastName(adminUserDto.getLastName());
        user.setEmail(adminUserDto.getEmail());
        StorageConfigDto configDto = storageConfigService.getConfigurationsWithoutDetail(adminUserDto.getSourceId());
        if (configDto == null) {
            throw new InvalidRequestException("Invalid source for id: " + adminUserDto.getSourceId());
        }
        user.setSourceId(adminUserDto.getSourceId());
        user.setGender(Gender.valueOf(adminUserDto.getGender()));
        user.setPassword(passAfterEncode);
        userDao.insertSelective(user);
        int userId = user.getId();
        // init dir
        dirService.createDefaultDir(userId);
        // init permission
        userPermissionService.changePermission(userId, Arrays.asList(PermissionConstant.DOWNLOAD.toString(), PermissionConstant.UPLOAD.toString()), null);
        // send email
        mailSenderService.sendAddUserMail(adminUserDto.getEmail(), pass);
    }

    @Override
    public void deleteUser(int userId) {
        User user = userDao.selectByPrimaryKey(new User(userId));
        if (user == null) {
            throw new InvalidRequestException("cannot found user, id:" + userId);
        }
        userDao.deleteUser(userId);
    }

    @Override
    public UserProfile getUserProfile() {
        int userId = currentAccount.getUserInfo().getId();
        User user = userDao.selectByPrimaryKey(new User(userId));
        List<String> permissions = userPermissionService.getPermissionListByUserId(userId).stream().map(Permission::getPermissionName).collect(Collectors.toList());
        return  mapUserToUserProfile(user, permissions);
    }

    @Override
    public UserProfile modifyUserProfile(UserProfile modifyProfile) {
        int userId = currentAccount.getUserInfo().getId();
        User user = new User(userId);
        String password = modifyProfile.getPassword();
        if(!StringUtils.isBlank(password)){
            if(password.length() < 6){
                throw new InvalidRequestException("Password length cannot less than 6.");
            }
            String encodedPassword = PasswordUtil.encodePassword(password);
            user.setPassword(encodedPassword);
        }
        user.setGender(Gender.valueOf(modifyProfile.getGender()));
        user.setFirstName(modifyProfile.getFirstName());
        user.setLastName(modifyProfile.getLastName());
        user.setBirthday(modifyProfile.getBirthday());
        userDao.updateByPrimaryKeySelective(user);
        currentAccount.getUserInfo().setFirstName(modifyProfile.getFirstName());
        currentAccount.getUserInfo().setLastName(modifyProfile.getLastName());
        return getUserProfile();
    }

    @Override
    public User getUserByEmail(String emailAddress) {
        return userDao.getUserByEmail(emailAddress);
    }

    private UserProfile mapUserToUserProfile(User user, List<String> permissions) {
        UserProfile userDto = ModelMapperUtil.mapping(user, UserProfile.class);
        userDto.setGender(user.getGender().toString());
        userDto.setStorage(user.getMemorySize());
        userDto.setUsedStorage(user.getUsedSize());
        userDto.setUploadAccess(false);
        userDto.setDownloadAccess(false);
        if (!CollectionUtils.isEmpty(permissions)) {
            if (permissions.contains(PermissionConstant.UPLOAD.toString())) {
                userDto.setUploadAccess(true);
            }
            if (permissions.contains(PermissionConstant.DOWNLOAD.toString())) {
                userDto.setDownloadAccess(true);
            }
        }
        return userDto;
    }

    private AdminUserDto mapUserToAdminUserDto(User user, List<String> permissions, StorageConfigDto configDto) {
        AdminUserDto userDto = ModelMapperUtil.mapping(user, AdminUserDto.class);
        userDto.setGender(user.getGender().toString());
        userDto.setTotalStorage(user.getMemorySize());
        userDto.setUsedStorage(user.getUsedSize());
        userDto.setBlockedStatus(user.getBlocked());
        userDto.setSourceId(user.getSourceId());
        userDto.setUploadAccess(false);
        userDto.setDownloadAccess(false);
        if (!CollectionUtils.isEmpty(permissions)) {
            if (permissions.contains(PermissionConstant.UPLOAD.toString())) {
                userDto.setUploadAccess(true);
            }
            if (permissions.contains(PermissionConstant.DOWNLOAD.toString())) {
                userDto.setDownloadAccess(true);
            }
        }
        if (configDto != null) {
            userDto.setSourceName(configDto.getName());
            userDto.setSourceType(configDto.getType());
        }
        return userDto;
    }
}
