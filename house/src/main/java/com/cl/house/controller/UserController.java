package com.cl.house.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cl.house.common.constants.CommonConstants;
import com.cl.house.common.model.User;
import com.cl.house.common.result.ResultMsg;
import com.cl.house.common.utils.HashUtils;
import com.cl.house.service.UserService;

@Controller
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/selectUser")
	@ResponseBody
	public List<User> selectUser(){
		return userService.selectUser();
	}
	
	
	@RequestMapping("/accounts/register")
	public String accountsRegister(User user,ModelMap modelMap) {
		if(user == null ||user.getName() ==null  ) {
			return "/user/accounts/register";
		}
		//用户验证
		ResultMsg msg = UserHelper.validate(user);
		try {
			if(msg.isSuccess() && userService.addUser(user)) {
				modelMap.put("email", user.getEmail());
				return "/user/accounts/registerSubmit";
			}else {
				return "redirect:/accounts/register?"+msg.asUrlParams();
			}
		} catch (Exception e) {
			 e.printStackTrace();
			 modelMap.put("errorMsg", "注册错误，邮件已经被注册！");
			 return "redirect:/accounts/register?"+ResultMsg.errorMsg("注册错误，邮件已经被注册！").asUrlParams();
		}
		
	}
	
	@RequestMapping("/accounts/verify")
	public String verify(String key) {
		
		boolean result = userService.enable(key);
		if(result) {
			return "redirect:/index?"+ResultMsg.successMsg("激活成功").asUrlParams();
		}else{
			return "redirect:/accounts/register?"+ResultMsg.errorMsg("激活失败，请确认连接是否过期！").asUrlParams();
		}
		
	}
	
	/**
	* @Title: signin  
	* @Description: TODO(登陆)  
	* @param @param req
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	@RequestMapping("/accounts/signin")
	public String  signin(HttpServletRequest req) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String target = req.getParameter("target");
		if(username ==null || password==null) {
			return "/user/accounts/signin";
		}
		User user = userService.auth(username,password);
		if(user ==null) {
			return "/user/accounts/signin?target="+target+"&username="+username+"&"+ResultMsg.errorMsg("用户名或者密码错误").asUrlParams();
		}else {
			HttpSession session = req.getSession(true);
			session.setAttribute(CommonConstants.USER_ATTRIBUTE, user);
			session.setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE, user);
			return StringUtils.isBlank(target)? "redirect:/index" :"redirect:"+target ;
		}
	}
	
	/**
	* @Title: logout  
	* @Description: TODO(登出)  
	* @param @param req
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	@RequestMapping("/accounts/logout")
	public String  logout(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		session.invalidate();
		return "redirect:/index";
		
	}
	
	 // ---------------------个人信息页-------------------------
	  /**
	   * 1.能够提供页面信息 2.更新用户信息
	   * 
	   * @param updateUser
	   * @param model
	   * @return
	   */
	  @RequestMapping("accounts/profile")
	  public String profile(HttpServletRequest req, User updateUser, ModelMap model) {
	    if (updateUser.getEmail() == null) {
	      return "/user/accounts/profile";
	    }
	    userService.updateUser(updateUser, updateUser.getEmail());
	    User query = new User();
	    query.setEmail(updateUser.getEmail());
	    List<User> users = userService.getUserByQuery(query);
	    req.getSession(true).setAttribute(CommonConstants.USER_ATTRIBUTE, users.get(0));
	    return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
	  }

	  /**
	   * 修改密码操作
	   * 
	   * @param email
	   * @param password
	   * @param newPassword
	   * @param confirmPassword
	   * @param mode
	   * @return
	   */
	  @RequestMapping("accounts/changePassword")
	  public String changePassword(String email, String password, String newPassword,
	    String confirmPassword, ModelMap mode) {
	    User user = userService.auth(email, password);
	    if (user == null || !confirmPassword.equals(newPassword)) {
	      return "redirct:/accounts/profile?" + ResultMsg.errorMsg("密码错误").asUrlParams();
	    }
	    User updateUser = new User();
	    updateUser.setPasswd(HashUtils.encryPassword(newPassword));
	    userService.updateUser(updateUser, email);
	    return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
	  }


	  /**
	   * 忘记密码
	   * @param username
	   * @param modelMap
	   * @return
	   */
	  @RequestMapping("accounts/remember")
	  public String remember(String username, ModelMap modelMap) {
	    if (StringUtils.isBlank(username)) {
	      return "redirect:/accounts/signin?" + ResultMsg.errorMsg("邮箱不能为空").asUrlParams();
	    }
	    userService.resetNotify(username);
	    modelMap.put("email", username);
	    return "/user/accounts/remember";
	  }
	  
	  @RequestMapping("accounts/reset")
	  public String reset(String key,ModelMap modelMap){
	    String email = userService.getResetEmail(key);
	    if (StringUtils.isBlank(email)) {
	      return "redirect:/accounts/signin?" + ResultMsg.errorMsg("重置链接已过期").asUrlParams();
	    }
	    modelMap.put("email", email);
	    modelMap.put("success_key", key);
	    return "/user/accounts/reset";
	  }
	  
	  @RequestMapping(value="accounts/resetSubmit")
	  public String resetSubmit(HttpServletRequest req,User user){
	    ResultMsg retMsg = UserHelper.validateResetPassword(user.getKey(), user.getPasswd(), user.getConfirmPasswd());
	    if (!retMsg.isSuccess() ) {
	      String suffix = "";
	      if (StringUtils.isNotBlank(user.getKey())) {
	         suffix = "email=" + userService.getResetEmail(user.getKey()) + "&key=" +  user.getKey() + "&";
	      }
	      return "redirect:/accounts/reset?"+ suffix  + retMsg.asUrlParams();
	    }
	    User updatedUser =  userService.reset(user.getKey(),user.getPasswd());
	    req.getSession(true).setAttribute(CommonConstants.USER_ATTRIBUTE, updatedUser);
	    return "redirect:/index?" + retMsg.asUrlParams();
	  }
}
