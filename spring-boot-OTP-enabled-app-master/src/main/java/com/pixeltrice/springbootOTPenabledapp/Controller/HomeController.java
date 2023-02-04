package com.pixeltrice.springbootOTPenabledapp.Controller;



import com.pixeltrice.springbootOTPenabledapp.DTO.AuthDto;
import com.pixeltrice.springbootOTPenabledapp.Services.OTPService;
import com.pixeltrice.springbootOTPenabledapp.Services.jwtServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    @Value("${spring.application.name}")
    String appName;
    @Autowired
    public jwtServices jwtServices;
    @Autowired
    public AuthenticationManager authenticationManager;
    @Autowired
    public OTPService otpService;
    //@PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/")
    public String homePage(Model model) {
    String message = " Welcome to Home Page";
        model.addAttribute("appName", appName);
        model.addAttribute("message", message);
        return "signin";
    }
  // @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/auntheticate")
    public String auntheticate(@RequestBody AuthDto authDto){

        Authentication authentication=  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(),authDto.getPassword()));
      if(authentication.isAuthenticated()) {
          return jwtServices.generateToken(authDto.getUsername());
      }else{
          throw new UsernameNotFoundException("invalid user request !");
      }




    }
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard(){
     return "dashboard";
    }
   // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/login")
    public String login() {
        return "signin";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user")
    public String user() {
        return "user";
    }
    @GetMapping("/aboutus")
    public String about() {
        return "aboutus";
    }
    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public @ResponseBody String logout(HttpServletRequest request, HttpServletResponse response){
       Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
       if (auth != null){    
       String username = auth.getName();
       //Remove the recently used OTP from server. 
       otpService.clearOTP(username);
       new SecurityContextLogoutHandler().logout(request, response, auth);
       }
   return "redirect:/login?logout";    
    }
}
