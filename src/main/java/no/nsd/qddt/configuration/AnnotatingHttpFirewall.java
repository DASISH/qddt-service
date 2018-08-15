package no.nsd.qddt.configuration;

import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Overrides the StrictHttpFirewall to log some useful information about blocked requests.
 */
public final class AnnotatingHttpFirewall extends StrictHttpFirewall
{
    /**
     * The name of the HTTP header representing a request that has been rejected by this firewall.
     */
    public static final String HTTP_HEADER_REQUEST_REJECTED = "X-HttpFirewall-RequestRejected";

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AnnotatingHttpFirewall.class.getName());

    /**
     * Default constructor.
     */
    public AnnotatingHttpFirewall()
    {
        super();
        return;
    }

    /**
     * Provides the request object which will be passed through the filter chain.
     *
     * @returns A FirewalledRequest (required by the HttpFirewall interface) which
     *          inconveniently breaks the general contract of ServletFilter because
     *          we can't upcast this to an HttpServletRequest. This prevents us
     *          from re-wrapping this using an HttpServletRequestWrapper.
     * @throws RequestRejectedException if the request should be rejected immediately.
     */
    @Override
    public FirewalledRequest getFirewalledRequest(final HttpServletRequest request) throws RequestRejectedException
    {
        try
        {
            return super.getFirewalledRequest(request);
        } catch (RequestRejectedException ex) {
            final String requestUrl = request.getRequestURL().toString();

            // Override some of the default behavior because some requests are
            // legitimate.
            if (requestUrl.contains("/auth/signin"))
            {
                // Do not block non-cookie serialized sessions. Google's crawler does this often.
            } else {
                // Log anything that is blocked so we can find these in the catalina.out log.
                // This will give us any information we need to make
                // adjustments to these special cases and see potentially
                // malicious activity.
                if (LOGGER.isLoggable(Level.WARNING))
                {
                    LOGGER.log(Level.WARNING, "Intercepted RequestBlockedException: Remote Host: " + request.getRemoteHost() + " User Agent: " + request.getHeader("User-Agent") + " Request URL: " + request.getRequestURL().toString());
                }

                // Mark this request as rejected.
                request.setAttribute(HTTP_HEADER_REQUEST_REJECTED, Boolean.TRUE);
            }

            // Suppress the RequestBlockedException and pass the request through
            // with the additional attribute.
            return new FirewalledRequest(request)
            {
                @Override
                public void reset()
                {
                    return;
                }
            };
        }
    }

    /**
     * Provides the response which will be passed through the filter chain.
     * This method isn't extensible because the request may already be committed.
     * Furthermore, this is only invoked for requests that were not blocked, so we can't
     * control the status or response for blocked requests here.
     *
     * @param response The original HttpServletResponse.
     * @return the original response or a replacement/wrapper.
     */
    @Override
    public HttpServletResponse getFirewalledResponse(final HttpServletResponse response)
    {
        // Note: The FirewalledResponse class is not accessible outside the package.
        return super.getFirewalledResponse(response);
    }
}