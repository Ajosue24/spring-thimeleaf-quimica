package com.proasecal.software.web.interceptor;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.proasecal.software.web.entity.seguridad.Permisos;
import com.proasecal.software.web.filter.AclFilter;
import com.proasecal.software.web.util.Util;

public class AclInterceptor extends HandlerInterceptorAdapter{
	private final Logger LOG = LoggerFactory.getLogger(AclFilter.class);
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String url = request.getRequestURI();
        //Indica si es un atributo del front si es asi no se valida
        Boolean isFrontValue=Util.ifJsorCssorJpg(url);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //admin con acceso a todo
        if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))){
            return true;
        }
        if(!isFrontValue){
            url = url.matches(".*\\d+.*")?Util.removeIdFromUrl(url):url;
            if(!url.contains("/login")) {
                Optional<List<Permisos>> collectPermisos = Util.userPermissions();
                if (collectPermisos.isPresent()) {
                    String finalUrl = url;
                    Boolean isPermission = collectPermisos.get().stream().anyMatch(
                            permiso -> permiso.getUrl().equals(finalUrl)||Util.urlCompareUrlDB(permiso.getUrl(),finalUrl));
                    /*isPermission= true;*/
                    if (!isPermission) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        request.setAttribute("acceso","401");
                        LOG.info("no tiene permisos para acceder a " + url);
                        throw new RuntimeException("error");
                    } else {
                        LOG.info("si tiene permisos para acceder a " + url);
                    }
                }
            }
        }
        return true;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, //
            Object handler, ModelAndView modelAndView) throws Exception {
    }
 
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, //
            Object handler, Exception ex) throws Exception {
    }
}
