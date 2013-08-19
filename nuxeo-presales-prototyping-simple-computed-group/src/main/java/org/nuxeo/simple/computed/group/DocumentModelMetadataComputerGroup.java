package org.nuxeo.simple.computed.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.platform.computedgroups.AbstractGroupComputer;
import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;
import org.nuxeo.runtime.api.Framework;

public class DocumentModelMetadataComputerGroup extends AbstractGroupComputer {

    public static final Log log = LogFactory.getLog(DocumentModelMetadataComputerGroup.class);

    private String groupPattern;

    private String whereClause;

    private String xpath;

    public DocumentModelMetadataComputerGroup(String whereClause,
            String groupPattern, String xpath) {
        this.whereClause = whereClause;
        this.xpath = xpath;
        this.groupPattern = groupPattern;
    }

    @Override
    public List<String> getAllGroupIds() throws Exception {
        List<String> groupIds = new ArrayList<String>();
        return groupIds;
    }

    @Override
    public List<String> getGroupMembers(String groupId) throws Exception {

        List<String> participants = new ArrayList<String>();
        return participants;
    }

    @Override
    public List<String> getGroupsForUser(NuxeoPrincipalImpl user)
            throws Exception {
        String username = user.getName();
        GetDocumentsFromUsername runner = new GetDocumentsFromUsername(
                getRepository(), whereClause, username, xpath);
        runner.runUnrestricted();

        List<String> groupIds = new ArrayList<String>();
        String groupId = null;

        for (String value : runner.result) {
            groupId = getGroupIdFromValue(value);
            log.debug("Virtual Group Id found: " + groupId);
            groupIds.add(groupId);
        }
        return groupIds;
    }

    @Override
    public List<String> getParentsGroupNames(String groupID) throws Exception {
        return new ArrayList<String>();
    }

    @Override
    public List<String> getSubGroupsNames(String groupID) throws Exception {
        return new ArrayList<String>();
    }

    @Override
    public boolean hasGroup(String groupId) throws Exception {
        return false;
    }

    protected class GetDocumentsFromUsername extends UnrestrictedSessionRunner {
        private static final String QUERY_PATTERN = "SELECT %s "
                + "FROM Document %s";

        protected String username;

        protected String xpath;

        protected String whereClausePattern;

        public List<String> result = new ArrayList<String>();

        protected GetDocumentsFromUsername(String repositoryName,
                String whereClause, String username, String xpath)
                throws Exception {
            super(repositoryName);
            this.username = username;
            this.whereClausePattern = whereClause;
            this.xpath = xpath;
        }

        @Override
        public void run() throws ClientException {
            String whereClause = String.format(whereClausePattern, username);
            String query = String.format(QUERY_PATTERN, xpath, whereClause);

            IterableQueryResult docs = session.queryAndFetch(query, "NXQL");
            for (Map<String, Serializable> doc : docs) {
                String value = (String) doc.get(xpath);
                if (value != null && !value.isEmpty()
                        && !result.contains(value)) {
                    result.add(value);
                }
            }

        }
    }

    private String getRepository() {
        RepositoryManager mgr = Framework.getLocalService(RepositoryManager.class);
        return mgr.getDefaultRepository().getName();
    }

    private String getGroupIdFromValue(String value) {
        return String.format(groupPattern, value);
    }

}
