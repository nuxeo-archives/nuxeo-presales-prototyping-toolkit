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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

/**
 * Advanced Function put into the Operation Chain as 'AdvFn' object into the Automation context.
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.7
 */
public class AdvancedFunctionsObject {

    /**
     * Concatenate a list of values into the first argument that is a list of
     * Value. Values to add will be explosed to be added to the result list if
     * this is a collection.
     *
     * @param <T>
     *
     * @param list List of values of type A
     * @param value Value can be instance of java.util.Collection<Object> or an
     *            array of Objects or simply a scalar Object. If Null, the
     *            parameter is ignored
     * @return the list that contains the list contain and value (see value
     *         description)
     * @exception ClientException if value if a collection but not contains only
     *                A values and type that extends A.
     * @exception ClientException if list is null
     * @exception xxxxx if in values there is at least one object type not
     *                compatible with the collection list
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> concatenateInto(List<T> list,
            Object... values) throws ClientException {

        if (list == null) {
            throw new ClientException("First parameter must not be null");
        }

        for (Object value : values) {
            if (value == null) {
                continue;
            }

            if (value instanceof Object[]) {
                for (Object subValue : (Object[]) value) {
                    if (subValue != null) {
                        list.add((T) subValue);
                    }
                }
                continue;
            }

            if (value instanceof Collection) {
                for (Object subValue : (Collection<Object>) value) {
                    if (subValue != null) {
                        list.add((T) subValue);
                    }
                }
                continue;
            }

            list.add((T) value);

        }
        return list;
    }

    /**
     * Idem than concatenateInto except that a new list is created.
     */
    public static <T> List<T> concatenateValues(Object... values) throws ClientException {

        List<T> result = new ArrayList<T>();
        return concatenateInto(result, values);
    }
}
