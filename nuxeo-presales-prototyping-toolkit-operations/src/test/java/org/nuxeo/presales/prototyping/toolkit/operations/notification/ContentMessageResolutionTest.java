/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Benjamin JALON <bjalon@nuxeo.com>
 */
package org.nuxeo.presales.prototyping.toolkit.operations.notification;

import static junit.framework.Assert.*;
import java.io.File;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.Environment;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.mail.Mailer.Message;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.presales.prototyping.toolkit.operations.notification.AdvancedSendEmail;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.7
 */
@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@Deploy({ "org.nuxeo.ecm.automation.core",
        "org.nuxeo.ecm.platform.notification.core",
        "org.nuxeo.ecm.platform.notification.api",
        "org.nuxeo.ecm.platform.url.api", "org.nuxeo.ecm.platform.url.core",
        "org.nuxeo.presales.prototyping.toolkit.operation" })
public class ContentMessageResolutionTest {

    private static final String MAIL_EXAMPLE = "Current doc: ${Document.path} title: ${Document['dc:title']}";

    protected DocumentModel src;

    @Inject
    AutomationService service;

    @Inject
    CoreSession session;

    private AdvancedSendEmail operation;


    @Before
    public void init() throws Exception {
        session.removeChildren(session.getRootDocument().getRef());
        session.save();

        src = session.createDocumentModel("/", "src", "File");
        src.setPropertyValue("dc:title", "Source");
        src = session.createDocument(src);
        session.save();
        src = session.getDocument(src.getRef());

        InputStream in = AdvancedSendEmailTest.class.getClassLoader().getResourceAsStream(
                "example.mail.properties");
        File file = new File(Environment.getDefault().getConfig(),
                "mail.properties");
        file.getParentFile().mkdirs();
        FileUtils.copyToFile(in, file);
        in.close();

        operation = new AdvancedSendEmail();
        operation.message = MAIL_EXAMPLE;
        operation.ctx = new OperationContext(session);
    }

    @Test
    public void shouldReturnContentResolvedByMvel() throws Exception {
        Message msg = operation.createContentMessage(src);
        assertEquals("Current doc: /src title: Source", msg.getContent());
    }


}
