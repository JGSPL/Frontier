package com.procialize.mrgeApp20.Zoom.initsdk;

public interface AuthConstants {

	// TODO Change it to your web domain
	public final static String WEB_DOMAIN = "zoom.us";

	// TODO Change it to your APP Key
	public final static String SDK_KEY = "NsaJuQn2CIL5iNqkU89E4bQBNwssePFHnRrj";

	// TODO Change it to your APP Secret
	public final static String SDK_SECRET = "p0MP4mq7GFpwV0JEDMkDwEzfgg8HKwk5bTjW";

	/**
	 * We recommend that, you can generate jwttoken on your own server instead of hardcore in the code.
	 * We hardcore it here, just to run the demo.
	 *
	 * You can generate a jwttoken on the https://jwt.io/
	 * with this payload:
	 * {
	 *     "appKey": "string", // app key
	 *     "iat": long, // access token issue timestamp
	 *     "exp": long, // access token expire time
	 *     "tokenExp": long // token expire time
	 * }
	 */
	public final static String SDK_JWTTOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ6b29tIiwibmFtZSI6InNuZWhhIiwiaWF0IjoyMDE2MjM5MDIyfQ.ob7boLH4dPi68W0ZNQAFhuEQJ2-xvcDm7pGcBRlyaEI";

}
