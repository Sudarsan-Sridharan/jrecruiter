package org.jrecruiter.web.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrecruiter.model.User;
import org.jrecruiter.service.UserService;
import org.jrecruiter.web.controller.BaseSimpleFormController;
import org.jrecruiter.web.forms.UserForm;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * List all the jobs. 
 * 
 * @author Gunnar Hillert
 * @version $Id: JobListAction.java 58 2006-10-16 03:45:45Z ghillert $
 *
 */
public class EditUserFormController extends BaseSimpleFormController  {
	
	/**
	 * Logger Declaration.
	 */
    private final Log LOGGER = LogFactory.getLog(EditUserFormController.class);
    
    /**
     * The service layer reference.
     */
    private UserService service;
    
    /**
     * Inject the service layer reference.
     * @param service 
     */
    public void setService(UserService service) {
		this.service = service;
	}

    /**
     * 
     */
    public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)
	throws Exception {
		LOGGER.debug("entering 'onSubmit' method...");

        User form = (User) command;
        User user = service.getUser(form.getUsername());

        user.setCompany(form.getCompany());
        user.setEmail(form.getEmail());
        user.setFax(form.getFax());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setPassword(form.getPassword());
        user.setPhone(form.getPhone());

        service.updateUser(user);
        return new ModelAndView(getSuccessView());

	}

    /* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
        String username;
        if ((request.getParameter("username") != null)
         && (request.isUserInRole("admin"))) {
            username = request.getParameter("username");
        } else {
            username = request.getRemoteUser();
        }

        final User user = service.getUser(username);
        UserForm form = new UserForm();
        
        form.setPassword2(user.getPassword());

        if (user.getRegisterDate() != null) {
        	form.setRegisterDate(user.getRegisterDate());
        }
        if (user.getUpdateDate() != null) {
        	form.setUpdateDate(user.getUpdateDate());
        }
        
        BeanUtils.copyProperties(form, user);
			
		return user;
	}
}