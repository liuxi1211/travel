package cn.itcast.travel.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private static String targetEncoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        targetEncoding = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(targetEncoding);
            response.setContentType("text/html; charset=" + targetEncoding);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        targetEncoding = null;
    }
}
