/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.html.link;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.tester.WicketTestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class StatelessLinkLifecycleTest extends WicketTestCase
{
	@Test
	void doesRegularCallbackSequence()
	{
		tester.executeUrl(
			"wicket/bookmarkable/org.apache.wicket.markup.html.link.StatelessLinkLifecycleTest$LinkInsideConstructor?-1.-statelessLink");
		TestPage page = (TestPage)tester.getLastRenderedPage();
		assertThat(page.statelessLink.callbackSequence, contains("onClick", "onConfigure"));
	}

	@Test
	void failingCallbackSequence()
	{
		tester.executeUrl(
			"wicket/bookmarkable/org.apache.wicket.markup.html.link.StatelessLinkLifecycleTest$LinkInsideOnInit?-1.-statelessLink");
		TestPage page = (TestPage)tester.getLastRenderedPage();
		assertThat(page.statelessLink.callbackSequence, contains("onClick", "onConfigure"));
	}

	static class TestLink extends StatelessLink<Void>
	{
		List<String> callbackSequence = new ArrayList<String>();

		public TestLink(String id)
		{
			super(id);
		}

		@Override
		protected void onConfigure()
		{
			super.onConfigure();
			callbackSequence.add("onConfigure");
		}

		@Override
		public void onClick()
		{
			callbackSequence.add("onClick");
		}
	}

	public static abstract class TestPage extends WebPage implements IMarkupResourceStreamProvider
	{
		TestLink statelessLink;

		@Override
		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream(
				"<html><body><a wicket:id=\"statelessLink\"></a></body></html>");
		}
	}

	public static class LinkInsideConstructor extends TestPage
	{

		public LinkInsideConstructor()
		{
			add(statelessLink = new TestLink("statelessLink"));
		}

	}

	public static class LinkInsideOnInit extends TestPage
	{
		@Override
		protected void onInitialize()
		{
			super.onInitialize();
			add(statelessLink = new TestLink("statelessLink"));
		}
	}

}
