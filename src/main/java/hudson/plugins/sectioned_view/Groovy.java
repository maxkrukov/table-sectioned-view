package hudson.plugins.sectioned_view;

import groovy.lang.Binding;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import hudson.EnvVars;
import hudson.Util;
import hudson.util.VariableResolver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jenkins.model.Jenkins;

import org.codehaus.groovy.control.CompilerConfiguration;

public class Groovy extends AbstractGroovy {

    public Groovy(File scriptSource) {
        super(scriptSource);
    }

    private static final Logger LOGGER = Logger.getLogger(Groovy.class.getName());
    private Binding binding = new Binding();
    private CompilerConfiguration compilerConfig = null;

    public String perform(String bindings, String classpath) throws InterruptedException, IOException {

        // Use HashMap as a backend for Binding as Hashtable does not accept nulls
        Map<Object, Object> parsedBindings = new HashMap<Object, Object>();
        parsedBindings.putAll(parseProperties(bindings));
        binding = new Binding(parsedBindings);

        if (classpath != null) {
            compilerConfig = new CompilerConfiguration();
            Map<String, String> env = EnvVars.masterEnvVars;
            // env.overrideAll(Jenkins.getInstance().getPluginManager().uberClassLoader. .getBuildVariables());
            VariableResolver<String> vr = new VariableResolver.ByMap<String>(env);
            if (classpath.contains("\n")) {
                compilerConfig.setClasspathList(parseClassPath(classpath, vr));
            } else {
                compilerConfig.setClasspath(Util.replaceMacro(classpath, vr));
            }
        }

        Object result = null;
        if (scriptSource.exists()) {
            try {
                result = this.execute(new GroovyCodeSource(scriptSource));
            } catch (IOException var6) {
                LOGGER.log(Level.WARNING, "Failed to execute " + scriptSource, var6);
            }
        }

        return (String) result;
    }

    protected GroovyShell createShell() {
        if (compilerConfig == null) {
            return new GroovyShell(Jenkins.getInstance().getPluginManager().uberClassLoader, this.binding);
        } else {
            return new GroovyShell(Jenkins.getInstance().getPluginManager().uberClassLoader, this.binding, this.compilerConfig);
        }
    }

    protected Object execute(GroovyCodeSource s) {
        Object result = null;

        try {
            result = this.createShell().evaluate(s);
        } catch (RuntimeException var4) {
            LOGGER.log(Level.WARNING, "Failed to run script " + s.getName(), var4);
        }

        return result;
    }

}
