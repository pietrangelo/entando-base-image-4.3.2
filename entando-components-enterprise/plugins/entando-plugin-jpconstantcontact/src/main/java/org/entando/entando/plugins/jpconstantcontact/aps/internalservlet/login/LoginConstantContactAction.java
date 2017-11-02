package org.entando.entando.plugins.jpconstantcontact.aps.internalservlet.login;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.ActionSupport;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpconstantcontact.aps.system.services.ConstantContactManager;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Alberto Piras
 */

public class LoginConstantContactAction extends BaseAction{
    
    
    public void LoginConstantContactAction(){}
    
    static HttpSession session;
    /**
     * Redirect user to Constant Contact Login with a correct URL
     * @return the success or the failure of login
     * @throws java.lang.Exception
     */
    public String doLogin()throws Exception, Throwable{
        setApi_code(this.getCtctManager().getConfig().getApiKey());
        setSecretCode(this.getCtctManager().getConfig().getSecret());
        /*stores in session the page code from which request is called*/
        Map pmap = this.getRequest().getParameterMap();
        session= this.getRequest().getSession();
        session.setAttribute("pagecode", this._pageCode);
        System.out.println("\n Login Started\n");
        StringBuffer urlRedirect= new StringBuffer();
        String base="https://oauth2.constantcontact.com/oauth2/oauth/siteowner/authorize?";
        String type="response_type=code";
        String clientId="&client_id="+api_code;
        String redirUrl = "&redirect_uri="+getCallbackUrl();
        urlRedirect.append(base);
        urlRedirect.append(type);
        urlRedirect.append(clientId);
        urlRedirect.append(redirUrl);
        try{
            ServletActionContext.getResponse().sendRedirect(urlRedirect.toString());
            return ActionSupport.NONE;
        }catch(Exception e){
            this.setMessageErrors("Verify your connection and ConstantContact Settings");
            return "errorConnection";
        }
        
    }
    
    /**
     * Completes the action login
     * @return the success or the failure of login
     */
    public String completeLoginAccess() throws Throwable {
        Map pmap = this.getRequest().getParameterMap();
        session = this.getRequest().getSession();
        if(null == pmap || pmap.isEmpty() || !(pmap.containsKey("code"))){
            System.out.println("No, Code In Request\n");
            return "generalError";
        } else {
            System.out.println("YES, Code In Request");
            String code = this.getRequest().getParameter("code");
            StringBuilder richiesta= new StringBuilder();
            String base="https://oauth2.constantcontact.com/oauth2/oauth/token?";
            String type="grant_type=authorization_code";
            String clientId="&client_id="+api_code;
            String client_secret="&client_secret="+secretCode;
            String authcode="&code="+code;
            String redirUrl = "&redirect_uri="+getCallbackUrl();
            richiesta.append(base);
            richiesta.append(type);
            richiesta.append(clientId);
            richiesta.append(client_secret);
            richiesta.append(authcode);
            richiesta.append(redirUrl);
            //HttpPost post = new HttpPost(richiesta.toString());
            try {
				URL url = new URL(richiesta.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                JSONTokener authresponse = new JSONTokener(rd);
                //String accessToken = this.getRequest().getParameter("access_token");
                JSONObject jauth = new JSONObject(authresponse);
                //String _accessToken = jauth.getString("access_token");
                System.out.println("Session started\n");
                /*store in session the access_token*/
                this.getRequest().getSession().setAttribute("access_token", jauth.getString("access_token"));
                //redirect page initialize to be given to jsp redirect
                _redirectPage = this.getPageUrl(this.getRequest().getSession().getAttribute("pagecode").toString());
                //***important: enstabilish a connection
                enstablishConnection();
                System.out.println("Logged: "+_connessione);
                return "success";
            } catch(Exception e) {
				System.out.println("Error in complete Login Access");
				return "errorConnection";
            }
        }
    }
    
    /**Execute the Logout of the User
     * @return "success" if the user has done a correct logout, "error" otherwise
     */
    public String doLogout(){
        try{
            _redirectPage=getPageUrl(this._pageCode);
            this.getRequest().getSession().removeAttribute("access_token");
            this.getRequest().getSession().removeAttribute("code");
            this.getRequest().getSession().removeAttribute("username");
            this.getRequest().getSession().removeAttribute("token");
            this.getRequest().getSession().removeAttribute("authorization_code");
            //***important: this cancel the previous ConstantContact connection
            _connessione=false;
            return SUCCESS;
        }catch(Exception e){
            System.out.println("Error in logout");
            return "errorConnection";
        }
    }
    
    /**Enstabilishes a ConstantContact Connection
     */
    public void enstablishConnection(){
        if(_connessione==false){
            System.out.println("\n\n#You are NOT logged on Constant Constact");
            try{
                _connessione = this.getCtctManager().connect(api_code,this.getRequest().getSession().getAttribute("access_token").toString());
                System.out.println("\n\n#Now You are logged on Constant Contact");
            }catch(Exception e){System.out.println("Connection error");}
        }
    }
    
    /**Get action path to complete login
     * @return the complete redirect url composed by: host, namespace, action
     * @throws java.lang.Throwable
     ***/
    public String getCallbackUrl() throws Throwable {
        StringBuilder redirUrl = new StringBuilder();
        String baseUrl = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
        //removes the last one "/" from baseUrl http://host/portalname/ to append the correct namespace /do/Ctct/completeLogin
        String baseUrlRew= new StringBuffer(baseUrl).deleteCharAt(baseUrl.length()-1).toString();
        if (null != baseUrlRew) {
            String namespace = ServletActionContext.getActionMapping().getNamespace();
            redirUrl.append(baseUrlRew);
            redirUrl.append(namespace);
            redirUrl.append("/");
            redirUrl.append(ACTION_CODE);
        } else {
            System.out.println("Error making callback URL");
        }
        return redirUrl.toString();
    }
    
    /**Get the page url
     * @param code : page code
     * @return the complete path of that page
     */
    protected String getPageUrl(String code) {
        String url = null;
        try{
            if (org.apache.commons.lang3.StringUtils.isNotBlank(code)) {
                IPage page = getPageManager().getPage(code);
                url = getUrlManager().createUrl(page, getCurrentLang(), null);
            } else {
                System.out.println("Couldn't generate the page URL because there is not a code param");
            }
        }catch(Exception e){
            System.out.println("Couldn't generate the page URL:"+e);
        }
        return url;
    }
    
    public IPageManager getPageManager() {
        return _pageManager;
    }
    public void setPageManager(IPageManager pageManager) {
        this._pageManager = pageManager;
    }
    public IURLManager getUrlManager() {
        return _urlManager;
    }
    public void setUrlManager(IURLManager urlManager) {
        this._urlManager = urlManager;
    }
    public ConfigInterface getConfigManager() {
        return _configManager;
    }
    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }
    public String getPageCode() {
        return _pageCode;
    }
    public void setPageCode(String code) {
        this._pageCode = code;
    }
    public String getRedirectPage() {
        return _redirectPage;
    }
    public void setRedirectPage(String _redirectPage) {
        this._redirectPage = _redirectPage;
    }
    public ConstantContactManager getCtctManager() {
        return _ctctManager;
    }
    public void setCtctManager(ConstantContactManager _ctctManager) {
        this._ctctManager = _ctctManager;
    }
    public boolean getConnessione() {
        return _connessione;
    }
    public void setConnessione(boolean _connessione) {
        this._connessione = _connessione;
    }
    
    public static String getApi_code() {
        return api_code;
    }
    
    public static void setApi_code(String api_code) {
        LoginConstantContactAction.api_code = api_code;
    }
    
    public static String getSecretCode() {
        return secretCode;
    }
    
    public static void setSecretCode(String secretCode) {
        LoginConstantContactAction.secretCode = secretCode;
    }
    
    public String getMessageErrors() {
        return _messageErrors;
    }
    
    public void setMessageErrors(String _messageErrors) {
        this._messageErrors = _messageErrors;
    }
    
    
    public String _redirectPage;
    public String _pageCode;
    private IPageManager _pageManager;
    private IURLManager _urlManager;
    private ConfigInterface _configManager;
    private static String api_code;
    public String _messageErrors;
    private static String secretCode;
    private static final String ACTION_CODE="completeLogin";
    private ConstantContactManager _ctctManager;
    protected static boolean _connessione;
}
