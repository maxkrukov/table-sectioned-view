package hudson.plugins.sectioned_view;

import hudson.Extension;

import java.io.File;
import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;

public class OB10VersionsViewSection extends SectionedViewSection {

    private String bindings;
    private String classpath;
    private String scriptname;

    @DataBoundConstructor
    public OB10VersionsViewSection(String name, Width width, Positioning alignment, String bindings, String classpath, String scriptname) {
        super(name, width, alignment);
        this.bindings = bindings;
        this.classpath = classpath;
        this.scriptname = scriptname;
    }

    public String getAppVersionsReverse() {
        String result = "Error! No result.";

        File scriptFile = new File(scriptname);
        Groovy groovy = new Groovy(scriptFile);

        try {
            result = groovy.perform(bindings, classpath);
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return result;
    }

    @Extension
    public static final class DescriptorImpl extends SectionedViewSectionDescriptor {

        public String getDisplayName() {
            return "OB10 Versions Section (git)";
        }
    }

    public String getBindings() {
        return bindings;
    }

    public String getClasspath() {
        return classpath;
    }

    public String getScriptname() {
        return scriptname;
    }
}
