package hudson.plugins.sectioned_view;

import hudson.Util;
import hudson.tasks.Builder;
import hudson.util.VariableResolver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.annotation.Nonnull;

/**
 * Shared functionality for Groovy builders
 * (nothing but script sources at the moment)
 *
 * @author dvrzalik
 */
public abstract class AbstractGroovy extends Builder {

    protected File scriptSource;

    public AbstractGroovy(File scriptSource) {
        this.scriptSource = scriptSource;
    }

    public File getScriptSource() {
        return scriptSource;
    }

    public static @Nonnull Properties parseProperties(final String properties) throws IOException {
        Properties props = new Properties();

        if (properties != null) {
            try {
                props.load(new StringReader(properties));
            } catch (NoSuchMethodError err) {
                props.load(new ByteArrayInputStream(properties.getBytes()));
            }
        }
        return props;
    }

    protected List<String> parseClassPath(String classPath, VariableResolver<String> vr) {
        List<String> cp = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(classPath);
        while (tokens.hasMoreTokens()) {
            cp.add(Util.replaceMacro(tokens.nextToken(), vr));
        }
        return cp;
    }
}
