//package natcash.proxy.config;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class SimpleCORSFilter implements Filter {
//
//	public String getAllowOrigin(HttpServletRequest request) {
//		String origin = request.getHeader("origin");
//		if (origin != null && origin.isEmpty() == false) {
//			return origin;
//		}
//		return "*";
//	}
//
//	@Override
//	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//			throws IOException, ServletException {
//		String allowOrigin = "*";
//		HttpServletResponse response = (HttpServletResponse) res;
//		if (req instanceof HttpServletRequest) {
//			allowOrigin = getAllowOrigin((HttpServletRequest) req);
//		}
//		response.setHeader("Access-Control-Allow-Origin", allowOrigin);
//		response.setHeader("Access-Control-Allow-Credentials", "true");
//		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//		response.setHeader("Access-Control-Max-Age", "3600");
//		response.setHeader("Access-Control-Allow-Headers",
//				"authorization, x-auth-token, Content-Type, Accept, X-Requested-With, remember-me");
//		chain.doFilter(req, res);
//	}
//
//	@Override
//	public void destroy() {
//
//	}
//
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//
//	}
//
//}