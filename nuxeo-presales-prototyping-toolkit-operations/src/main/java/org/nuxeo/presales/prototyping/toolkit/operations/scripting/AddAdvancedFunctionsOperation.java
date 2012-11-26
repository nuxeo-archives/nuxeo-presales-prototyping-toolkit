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
package org.nuxeo.presales.prototyping.toolkit.operations.scripting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;

/**
 * See operation description
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.7
 */
@Operation(id=AddAdvancedFunctionsOperation.ID, category=Constants.CAT_SCRIPTING, label="Add Advanced Functions", description="Add this operation at the begining of each chain if you want to use new functions into your scripting strings. This operation add advanced functions named 'AdvFn' to manage list of items, ... <p> Managing list of items : <ul> <li>AdvFn.concatenateInto(ListOfA, B, ListOfC, ArrayOfD) will add into ListOfA: B, each item of ListOfC, each item of ArrrayOfD and <b>will return ListOfA modified</b>.</li> <li>concatenateValues will do the same except that a new List will be created to support all values.</li></ul>")
public class AddAdvancedFunctionsOperation {

    public static final String ID = "AddAdvancedFunctions";

    public static final String ADVANCED_FUNCTION_NAME = "AdvFn";

    protected static final Log log = LogFactory.getLog(AddAdvancedFunctionsOperation.class);

    @Context
    protected OperationContext ctx;

    @OperationMethod
    public void run() {
        ctx.put(ADVANCED_FUNCTION_NAME, new AdvancedFunctionsObject());
    }

}
