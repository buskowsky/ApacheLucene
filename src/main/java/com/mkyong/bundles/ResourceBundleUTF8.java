package com.mkyong.bundles;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceBundleUTF8 extends ResourceBundle
{
    public static final String BUNDLE_NAME = "messages";

    protected static final String BUNDLE_EXTENSION = "properties";

    protected static final String CHARSET = "UTF-8";

    protected static final Control UTF8_CONTROL = new UTF8Control();

    public ResourceBundleUTF8()
    {
        Locale loc = null;

        try
        {
            loc = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        }
        catch (final Exception e)
        {
            loc = Locale.ENGLISH;
        }
        setParent(ResourceBundle.getBundle(BUNDLE_NAME, loc, UTF8_CONTROL));
    }

    @Override
    protected Object handleGetObject(final String key)
    {
        return this.parent.getObject(key);
    }

    @Override
    public Enumeration<String> getKeys()
    {
        return this.parent.getKeys();
    }

    protected static class UTF8Control extends Control
    {
        public UTF8Control()
        {
            super();
        }

        @Override
        public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IOException {

            final String bundleName = toBundleName(baseName, locale);
            final String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
            ResourceBundle bundle = null;

                InputStream stream = null;
                if (reload)
                {
                    final URL url = loader.getResource(resourceName);
                    if (url != null)
                    {
                        final URLConnection connection = url.openConnection();
                        if (connection != null)
                        {
                            connection.setUseCaches(false);
                            stream = connection.getInputStream();
                        }
                    }
                }
                else
                {
                    stream = loader.getResourceAsStream(resourceName);
                }
                if (stream != null)
                {
                    try
                    {
                        bundle = new PropertyResourceBundle(new InputStreamReader(stream, CHARSET));
                    }
                    finally
                    {
                        stream.close();
                    }
                }

            return bundle;
        }
    }
}

