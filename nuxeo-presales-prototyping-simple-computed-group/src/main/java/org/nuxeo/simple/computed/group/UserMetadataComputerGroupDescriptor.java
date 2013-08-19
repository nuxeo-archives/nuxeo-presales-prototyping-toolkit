package org.nuxeo.simple.computed.group;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.computedgroups.GroupComputer;
import org.nuxeo.ecm.platform.computedgroups.GroupComputerDescriptor;

@XObject("userMetadataGroupComputer")
public class UserMetadataComputerGroupDescriptor implements
        SimpleComputerGroupDescriptorInterface {

    @XNode("@xpath")
    public String xpath;

    @XNode("@groupPattern")
    public String groupPattern = "%s";

    @XNode("@name")
    public String name;

    @XNode("@enabled")
    public boolean enabled = true;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public GroupComputerDescriptor getGCD() {
        return new GCDDynamic(name, xpath, groupPattern);
    }

    private class GCDDynamic extends GroupComputerDescriptor {
        private static final long serialVersionUID = 1L;

        private String name;

        private String xpath;

        private String groupPattern;

        public GCDDynamic(String name, String xpath, String groupPattern) {
            this.name = name;
            this.xpath = xpath;
            this.groupPattern = groupPattern;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public GroupComputer getComputer() throws ClientException {
            return new UserMetadataComputerGroup(xpath, groupPattern);
        };
    }

    @Override
    public boolean isContributionValid() {
        if (xpath == null || xpath.isEmpty() || groupPattern == null || groupPattern.isEmpty()) {
            return false;
        }
        return true;
    }


}
