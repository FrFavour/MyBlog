package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.MenuMapper;
import com.my.blog.domain.entity.Menu;
import com.my.blog.service.IMenuService;
import com.my.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2024-04-23
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (id == 1L) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON).eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = menuMapper.selectList(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
//否则返回所具有的权限
        return menuMapper.selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
//判断是否是管理员
        if (SecurityUtils.isAdmin()) {
//如果是，获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
//否则，获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuByUserId(userId);
        }
//构建tree
//先找出一级菜单，然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus);
        return menuTree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus) {
        List<Menu> menuTree = menus.stream()
// 获取一级菜单
                .filter(menu -> menu.getParentId().equals(0L))
// 查询并设置一级菜单下的子菜单
                .map(menu -> menu.setChildren(getChildren(menus, menu.getId())))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(List<Menu> menus, Long menuId) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menuId))
                .collect(Collectors.toList());
        return childrenList;
    }

}
