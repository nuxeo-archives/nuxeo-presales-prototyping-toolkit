# Description
This plugin provides helpers which make it super simple to display a form in a fancybox and pass the values to an automation chain.

# Using the plugin 
Displaying a form in a fancybox is as simple as adding a new action button. below is an example of how to use the plugin:

  <extension target="org.nuxeo.ecm.platform.actions.ActionService" point="actions">
    <!-- A download action which display a form with additional parameters which are then passed to an automation chain -->
	<action id="Download" order="0" label="testFancy" type="fancybox">
      <category>DOCUMENT_UPPER_ACTION</category>
      <properties>
	    <!-- The fancybox container layout -->
        <property name="include">/incl/fancy_box_container.xhtml</property>
	    <!-- The form layout to display in the layout -->
        <property name="layout">DownloadLayout@edit</property>
		<!-- The chain to start when the user hits the process button -->
        <property name="chain">testChain</property>
		<!-- The fancybox main title -->
        <property name="title">Download additional parameters</property>
		<!-- The process button label -->
        <property name="processLabel">Download</property>
      </properties>
    </action>
  </extension>

The value entered in the form by the user are available as a context variable in the automation chain: Context["data"]
This variable is an instance of the DocumentModel interface.

This project is not unit-tested, please use with care

