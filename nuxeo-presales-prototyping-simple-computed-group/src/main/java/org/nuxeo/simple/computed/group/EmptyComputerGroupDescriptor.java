package org.nuxeo.simple.computed.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.computedgroups.AbstractGroupComputer;
import org.nuxeo.ecm.platform.computedgroups.GroupComputer;
import org.nuxeo.ecm.platform.computedgroups.GroupComputerDescriptor;
import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;

/**
 * Used to disable the computer group.
 *
 * @since 5.6
 *
 */
public class EmptyComputerGroupDescriptor extends GroupComputerDescriptor {

    private static final long serialVersionUID = 1L;

    public static final Log log = LogFactory.getLog(EmptyComputerGroupDescriptor.class);

    public EmptyComputerGroupDescriptor(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GroupComputer getComputer() throws ClientException {
        return new EmptyComputerGroup();
    };

    private class EmptyComputerGroup extends AbstractGroupComputer {

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
            return new ArrayList<String>();
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
}
