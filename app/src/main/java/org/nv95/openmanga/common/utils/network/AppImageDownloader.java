package org.nv95.openmanga.common.utils.network;

import android.content.Context;
import android.net.Uri;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import org.nv95.openmanga.core.providers.MangaProvider;

import java.io.IOException;
import java.net.HttpURLConnection;

import info.guardianproject.netcipher.NetCipher;

/**
 * Created by koitharu on 24.12.17.
 */

public class AppImageDownloader extends BaseImageDownloader {

	public AppImageDownloader(Context context) {
		super(context);
	}

	public AppImageDownloader(Context context, int connectTimeout, int readTimeout) {
		super(context, connectTimeout, readTimeout);
	}

	@Override
	protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
		final String provider = extra != null && extra instanceof String ? (String)extra : null;
		String nurl = url.startsWith("https:") ? "http" + url.substring(5) : url;
		nurl = Uri.encode(nurl, ALLOWED_URI_CHARS);
		final HttpURLConnection connection = NetCipher.getHttpURLConnection(nurl);
		connection.setConnectTimeout(connectTimeout);
		connection.setReadTimeout(readTimeout);
		final String domain = provider != null ? MangaProvider.getDomain(provider) : connection.getURL().getHost();
		final String cookie = CookieStore.getInstance().get(domain);
		if (cookie != null) {
			connection.addRequestProperty("cookie", cookie);
		}
		connection.addRequestProperty(NetworkUtils.HEADER_REFERER, domain);
		connection.addRequestProperty(NetworkUtils.HEADER_USER_AGENT, NetworkUtils.USER_AGENT_DEFAULT);
		return connection;
	}
}
