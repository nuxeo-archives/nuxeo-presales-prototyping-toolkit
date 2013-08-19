package org.nuxeo.simple.computed.group;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.computedgroups.GroupComputer;
import org.nuxeo.ecm.platform.computedgroups.GroupComputerDescriptor;

@XObject("documentMetadataGroupComuter")
public class DocumentModelMetadataComputerGroupDescriptor implements SimpleComputerGroupDescriptorInterface {

    @XNode("@whereClause")
    public String whereClause = "";

    @XNode("@groupPattern")
    public String groupPattern = "%s";

    @XNode("@xpath")
    public String xpathSelector = "ecm:uuid";

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
        return new GCDDynamic(name, whereClause, groupPattern, xpathSelector);
    }

    private class GCDDynamic extends GroupComputerDescriptor {
        private static final long serialVersionUID = 1L;

        private String name;

        private String whereClause;

        private String groupPattern;

        private String xpath;

        public GCDDynamic(String name, String whereClause, String groupPattern, String xpath) {
            this.name = name;
            this.whereClause = "WHERE " + whereClause;
            this.groupPattern = groupPattern;
            this.xpath = xpath;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public GroupComputer getComputer() throws ClientException {
            return new DocumentModelMetadataComputerGroup(whereClause, groupPattern, xpath);
        };
    }

    @Override
    public boolean isContributionValid() {
        if (whereClause == null || whereClause.isEmpty() || groupPattern == null || groupPattern.isEmpty()) {
            return false;
        }
        return true;
    }


}
