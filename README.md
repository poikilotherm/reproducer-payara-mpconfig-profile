# reproducer-payara-mpconfig-profile

This is a reproducer to check if Payara has a bug in their implementation of MicroProfile Config.

## Context

MicroProfile Config provides three default sources: a) property source via `META-INF/microprofile-config.properties`,
b) environment variables and c) system properties. The [spec defines an ordinality](https://download.eclipse.org/microprofile/microprofile-config-3.0/microprofile-config-spec-3.0.html#default_configsources) 
of these sources, where b) overrules a) and c) overrules a) and b).

In addition, the [spec defines a profile mechanism](https://download.eclipse.org/microprofile/microprofile-config-3.0/microprofile-config-spec-3.0.html#configprofile).
Prefixing a setting name with `%<profilename>.` means a setting can have a different value depending on the profile.

## Problem

In Dataverse, the `microprofile-config.properties` file has some profiled setting like `%ct.dataverse.siteUrl`.
When deploying the Dataverse application to a Payara server in a container, this setting could not be overridden when
providing a system property.

TODO: add link here?

## Reproducing

This project tries to reproduce the problem at different stages and angles, also enabling quick tests with different
versions of Payara.

There are 3 settings that the `TestMpConfig` class retrieves in three different methods:
- `test.setting`
- `test.setting.2`
- `test.setting.3`

### Baseline

To create a baseline, there are unit tests in place, using [SmallRye Config](https://smallrye.io/smallrye-config)
(not Payara) as a reference point: `TestMpConfigTest` and `ProfiledTestMpConfigTest`.

These tests ensure:
- `test.setting` is retrieved without a profile and no system property from the properties file
- `test.setting` is retrieved from a system property and not the properties file when set
- `test.setting.2`, which has a `%testprofile.test.setting.2=barbecue`, is retrieved as that value when profile
  is set and no system property given
- `test.setting.2`, is not retrieved from the profiled setting in the properties file but from a system
  property `test.setting.2` (not profiled!) when set 

This works as expected, thus marking a baseline.

Execute these tests with `mvn test` (or as part of `mvn verify`).

### Arquillian Managed Payara

As a next step, there is an Arquillian test in place. It starts a Payara application server and deploys
the `TestMpConfig` as an app scoped CDI bean. In addition, the Payara [environment-setup](https://github.com/payara/ecosystem-arquillian-connectors/tree/master/environment-setup)
transfers `src/main/resources/system-properties.properties` to the managed server and sets the contained
properties. The `PayaraConfigIT` creates the deployment and provides the test methods that are executed within
the Arquillian context on the server.

This managed server will not have an active profile. As a consequence, if the bug would be present, 
retrieving `test.setting.2` would return the value from the properties file (`barbecue`).
If the bug is not present, it would throw a `NoSuchElementException`.

The `system-properties.properties` file makes `test.setting` and `test.setting.3` system properties. If the bug is 
present, `test.setting.3`, which has a `%testprofile.test.setting.3` in `microprofile-config.properties`, would not
be overridden. (The spec says: *Conforming implementations are required to search for a configuration source with 
the highest ordinal (priority) that provides either the property name or the "profile-specific" property name.
If the configuration source provides the "profile-specific" name, the value of the "profile-specific" property will be
returned. If it doesn’t contain the "profile-specific" name, the value of the plain property will be returned.* That
means, if there is a source providing an unprofiled setting with a higher ordinal, it will win
over `microprofile-config.properties`.)

Both cases are working as expected, no bug present.

Execute these tests via `mvn verify`.