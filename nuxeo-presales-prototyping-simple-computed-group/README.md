# Description
Add a service that simplifies the configuration of simple computed groups

# Using the plugin

The project deploy a new service with an extension point to contribute simple Computed groups.

Simple Computed groups is Nuxeo feature that lets you define group computation evaluated during the user connection. This computation is expressed by a java code.

Most of the time, this computation is just fetch information from the user metadata or a property from a list of document. This service helps you to define this kind of Computed Groups by a simple XML configuration without Java Code.

## Computed Groups based on a Metadata in UserProfile

Sometimes, you will need to define a group based on a metadata in the user. For instance, in the LDAP directory users have rank (in army), a level of accreditation. You would like add a group based on this information:

* if the user is General, you want him to be in Rank_General group.
* if the user have internation 