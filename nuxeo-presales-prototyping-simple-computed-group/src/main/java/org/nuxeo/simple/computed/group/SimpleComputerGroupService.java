/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bjalon
 */

package org.nuxeo.simple.computed.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.computedgroups.ComputedGroupsService;
import org.nuxeo.ecm.platform.computedgroups.ComputedGroupsServiceImpl;
import org.nuxeo.ecm.platform.computedgroups.GroupComputerChainDescriptor;
import org.nuxeo.ecm.platform.computedgroups.GroupComputerDescriptor;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class SimpleComputerGroupService extends DefaultComponent {

    private static final Log log = LogFactory.getLog(SimpleComputerGroupService.class);

    private List<String> computedGroupNames = new ArrayList<String>();

    private ComputedGroupsServiceImpl service;

    @Override
    public void registerContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (contribution instanceof SimpleComputerGroupDescriptorInterface) {
            SimpleComputerGroupDescriptorInterface umcg = ((SimpleComputerGroupDescriptorInterface) contribution);
            GroupComputerDescriptor gcd;
            if (umcg.isEnabled()) {
                if (umcg.isContributionValid()) {
                    log.error("Bad contribution format "
                            + contributor.getName());
                }

                gcd = umcg.getGCD();
            } else {
                gcd = new EmptyComputerGroupDescriptor(umcg.getName());
            }
            getService().registerContribution(gcd, "computer", contributor);

            if (!computedGroupNames.contains(umcg.getName())) {
                computedGroupNames.add(umcg.getName());
                GCCDDynamic gccd = new GCCDDynamic(umcg.getName());
                getService().registerContribution(gccd, "computerChain",
                        contributor);
            }
        }
    }

    @Override
    public void unregisterContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (contribution instanceof SimpleComputerGroupDescriptorInterface) {
            SimpleComputerGroupDescriptorInterface umcg = ((SimpleComputerGroupDescriptorInterface) contribution);
            GroupComputerDescriptor gcd = umcg.getGCD();
            getService().unregisterContribution(gcd, "computer", contributor);

            if (computedGroupNames.contains(umcg.getName())) {
                computedGroupNames.remove(umcg.getName());
                GCCDDynamic gccd = new GCCDDynamic(umcg.getName());
                getService().unregisterContribution(gccd, "computerChain",
                        contributor);
            }
        }
    }

    public ComputedGroupsServiceImpl getService() {
        if (service == null) {
            service = (ComputedGroupsServiceImpl) Framework.getLocalService(ComputedGroupsService.class);
        }
        return service;
    }

    private class GCCDDynamic extends GroupComputerChainDescriptor {

        private static final long serialVersionUID = 1L;

        public String name;

        public GCCDDynamic(String name) {
            this.name = name;
        }

        public boolean isAppend() {
            return true;
        }

        public List<String> getComputerNames() {
            ArrayList<String> result = new ArrayList<String>();
            result.add(name);
            return result;
        }

    }

}