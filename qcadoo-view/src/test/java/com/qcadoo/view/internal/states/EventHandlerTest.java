/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo Framework
 * Version: 1.2.0
 *
 * This file is part of Qcadoo.
 *
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.qcadoo.view.internal.states;

import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;

import com.qcadoo.view.api.ComponentState;
import com.qcadoo.view.api.ViewDefinitionState;
import com.qcadoo.view.internal.components.form.FormComponentPattern;
import com.qcadoo.view.internal.components.form.FormComponentState;

public class EventHandlerTest {

    @Test
    public void shouldCallEventMethod() throws Exception {
        // given
        ViewDefinitionState viewDefinitionState = mock(ViewDefinitionState.class);

        FormComponentPattern pattern = mock(FormComponentPattern.class);
        given(pattern.getExpressionNew()).willReturn(null);
        given(pattern.getExpressionEdit()).willReturn(null);
        FormComponentState component = new FormComponentState(pattern);
        component.setFieldValue(13L);

        // when
        component.performEvent(viewDefinitionState, "clear");

        // then
        assertNull("value is " + component.getFieldValue(), component.getFieldValue());

    }

    @Test
    public void shouldCallCustomEventMethod() throws Exception {
        // given
        ViewDefinitionState viewDefinitionState = mock(ViewDefinitionState.class);

        CustomEventBean bean = mock(CustomEventBean.class);
        FormComponentPattern pattern = mock(FormComponentPattern.class);
        given(pattern.getExpressionNew()).willReturn(null);
        given(pattern.getExpressionEdit()).willReturn(null);
        FormComponentState component = new FormComponentState(pattern);
        component.registerCustomEvent("custom", bean, "customMethod", null);

        // when
        component.performEvent(viewDefinitionState, "custom", "arg0", "arg1");

        // then
        Mockito.verify(bean).customMethod(viewDefinitionState, component, new String[] { "arg0", "arg1" });
    }

    @Test
    public void shouldCallMultipleEventMethods() throws Exception {
        // given
        ViewDefinitionState viewDefinitionState = mock(ViewDefinitionState.class);

        FormComponentPattern pattern = mock(FormComponentPattern.class);
        given(pattern.getExpressionNew()).willReturn(null);
        given(pattern.getExpressionEdit()).willReturn(null);
        FormComponentState component = new FormComponentState(pattern);
        component.setFieldValue(13L);

        CustomEventBean bean1 = mock(CustomEventBean.class);
        component.registerCustomEvent("clear", bean1, "customMethod", null);

        CustomEventBean bean2 = mock(CustomEventBean.class);
        component.registerCustomEvent("clear", bean2, "customMethod", null);

        // when
        component.performEvent(viewDefinitionState, "clear");

        // then
        assertNull("value is " + component.getFieldValue(), component.getFieldValue());
        Mockito.verify(bean1).customMethod(viewDefinitionState, component, new String[0]);
        Mockito.verify(bean2).customMethod(viewDefinitionState, component, new String[0]);
    }

    @Test
    public void shouldNotThrowExceptionWhenEventNotExists() throws Exception {
        // given
        ViewDefinitionState viewDefinitionState = mock(ViewDefinitionState.class);

        FormComponentPattern pattern = mock(FormComponentPattern.class);
        given(pattern.getExpressionNew()).willReturn(null);
        given(pattern.getExpressionEdit()).willReturn(null);
        FormComponentState component = new FormComponentState(pattern);
        component.setFieldValue(13L);

        // when
        component.performEvent(viewDefinitionState, "noSuchMethod");
    }

    // shouldNotCallEventBeforeInitialazing

    private interface CustomEventBean {

        void customMethod(ViewDefinitionState viewDefinitionState, ComponentState componentState, String[] args);

    }

}
