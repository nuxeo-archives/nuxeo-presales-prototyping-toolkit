package org.nuxeo.simple.computed.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.computedgroups.AbstractGroupComputer;
import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;

public class UserMetadataComputerGroup extends AbstractGroupComputer {

    public static final Log log = LogFactory.getLog(UserMetadataComputerGroup.class);

    private String groupPattern;

    private String xpath;

    public UserMetadataComputerGroup(String xpath, String groupPattern) {
        this.xpath = xpath;
        this.groupPattern = groupPattern;
    }

    @Override
    public List<String> getAllGroupIds() throws Exception {
        return new ArrayList<String>();
    }

    @Override
    public List<String> getGroupMembers(String groupId) throws Exception {
        return new ArrayList<String>();
    }

    @Override
    public List<String> getGroupsForUser(NuxeoPrincipalImpl user)
            throws Exception {
        String value = (String) user.getModel().getPropertyValue(xpath);

        if (value == null || "".equals(value.trim())) {
            return new ArrayList<String>();
        }

        ArrayList<String> result = new ArrayList<String>();
        result.add(String.format(groupPattern, value));

        return result;
    }

    @Override
    public List<String> getParentsGroupNames(String arg0) throws Exception {
        return new ArrayList<String>();
    }

    @Override
    public List<String> getSubGroupsNames(String arg0) throws Exception {
        return new ArrayList<String>();
    }

    @Override
    public boolean hasGroup(String groupId) throws Exception {
        return false;
    }
}
