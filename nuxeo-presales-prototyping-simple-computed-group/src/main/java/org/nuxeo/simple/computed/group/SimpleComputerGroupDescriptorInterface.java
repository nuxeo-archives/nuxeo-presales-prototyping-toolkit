package org.nuxeo.simple.computed.group;

import org.nuxeo.ecm.platform.computedgroups.GroupComputerDescriptor;

public interface SimpleComputerGroupDescriptorInterface {

    public GroupComputerDescriptor getGCD();

    public String getName();

    public boolean isContributionValid();

    boolean isEnabled();

}
