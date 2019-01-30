package hudson.plugins.sectioned_view;

import hudson.Extension;
import java.io.File;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;

public class TableViewSection extends SectionedViewSection {

    private String script;
    private String lang;
    private String scriptPath;

    @DataBoundConstructor
    public TableViewSection(String name, Width width, Positioning alignment, String script, String lang, String scriptPath) {
        super(name, width, alignment);
        this.script = script;
        this.lang = lang;
        this.scriptPath = scriptPath;
    }

    public String getStdout() throws InterruptedException, IOException {
        String out = SysExec.run(script, scriptPath , lang);
        String[] abc = out.split("\n");
        String result = CsvToTable.main(abc);
        return result;
    }

    @Extension
    public static final class DescriptorImpl extends SectionedViewSectionDescriptor {
        public String getDisplayName() {
            return "Table sectioned view";
        }
    }

    public String getLang() {
        return lang;
    }

    public String getScript() {
        return script;
    }

    public String getScriptPath() {
        return scriptPath;
    }

}
