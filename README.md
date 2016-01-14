# oVirt Engine API Model

## Introduction

This project contains the specification of the oVirt Engine API, also
known ans the _model_.

The specification of the API is written using Java as the supporting
language.

Data types are represented by Java interfaces. For example, the `Vm.java`
file contains the specification of the `Vm` entity, which looks like
this:

```java
@Type
public interface Vm extends VmBase {
    String stopReason();
    Date startTime();
    Date stopTime();
    ...
}
```

The methods  of these interfaces represent the attributes of the data
types, including their type and name.

Services API are also represented by Java interfaces. For example, the
`VmService.java` file contains the specification of the `Vm` service, and
it has content similar to this:

```java
@Service
public interface VmService extends MeasurableService {
    interface Start {
        @In Boolean pause();
        @In Vm vm();
        @In Boolean useCloudInit();
        @In Boolean useSysprep();
        @In Boolean async();
    }
    ...
}
```

Operations are represented as nested interfaces. The names of these nested
interfaces correspond to the names of the operations, and the methods correspond
to the parameters of the operations.

The Java language supports adding documentation in the code itself, using the
_Javadoc_ comments. These comments start with `/**` and end with `*/` and can
be added before the definition of any element, like interfaces and methods. These
_Javadoc_ comments are the mechanism that we use to document the specification. For
example, the `Vm` type can be documented modifying the `Vm.java` file like this:

```java
/**
 * Represents a virtual machine.
 */
@Type
public interface Vm extends VmBase {
    ...
}
```

Attributes can be documented in a similar way, just placing the _Javadoc_ comment
before the definition of the method that represents that attribute:

```java
/**
 * Represents a virtual machine.
 */
@Type
public interface Vm extends VmBase {
    /**
     * Contains the reason why this virtual machine was stopped. This reason is
     * provided by the user, via the GUI or via the API.
     */
    String stopReason();
    ...
}
```

Same for services, their operations and their parameters:

```java
/**
 * This service manages a specific virtual machine.
 */
@Service
public interface VmService extends MeasurableService {

    /**
     * This operation will start the virtual machine managed by this
     * service, if it isn't already running.
     */
    interface Start {
        /**
         * Specifies if the virtual machine should be started in pause
         * mode. It is an optional parameter, if not given then the
         * virtual machine will be started normally.
         */
        @In Boolean pause();
        ...
    }
    ...
}
```

These _Javadoc_ comments are processed by tools that are part of the system
and it is used to automatically generate the reference documentation. You
can see an example of the generated documentation
[here](https://jhernand.fedorapeople.org/ovirt-api-explorer).

This documentation viewer will eventually be part of the oVirt Engine server
itself.

The _Javadoc_ comments have their own format, but it isn't used by the
our documentation tools. Instead of that the tools expect and support
_Markdown_, in particular the
[GitHub](https://help.github.com/articles/github-flavored-markdown)
variant. This means that _Javadoc_ comments can contain rich text and
examples. For example, you could write the following to better describe
the `Start` operation of the `Vm` service:

```java
/**
 * Specifies if the virtual machine should be started in pause
 * mode. It is an _optional_ parameter, if not given then the
 * virtual machine will be started normally.
 *
 * To use this parameter with the Python SDK you can use the
 * following code snippet:
 *
 * ` ` `python
 * # Find the virtual machine:
 * vm = api.vms.get(name="myvm")
 *
 * # Start the virtual machine paused:
 * vm.start(
 *   params.Action(
 *     pause=True
 *   )
 * )
 * ` ` `
 */
@In Boolean pause();
```

## Contributing documentation

### Cloning the `ovirt-engine-api-model` repository

The mechanism to contribute documentation is to modify the `.java`
source files. This source code is part of the `ovirt-engine-api-model`
project, which is hosted in
[gerrit.ovirt.org](http://gerrit.ovirt.org/ovirt-engine-api-model). So
the first step to be able to contribute is to register to that system
and prepare your environment for using `git`. For details see
[this](http://www.ovirt.org/Working_with_oVirt_Gerrit).

To summarize, once you have registered and prepared your system to
use `git`, this is the command that you need to execute in order to
clone the `ovirt-engine-api-model` source:

```
$ git clone gerrit.ovirt.org:ovirt-engine-api-model
```

### Locating the source file that you want to modify

The model source files are all inside the `src/main/java` directory, so
you will probably want to change to that directory:

```
$ cd src/main/java
```

This directory contains two sub-directories: `types` and `services`. The
first is for the specifications of data types and the second for the
specifications of services.

Files are named like the entities, so they should be easy to locate.

### Modifying the source files

You can use your favorite editor to modify the source files. Just make
sure to modify only the _Javadoc_ comments.

### Submitting the changes

Once you are happy with the changes that you made to the documentation
you can prepare and submit a patch. For example, lets assume that you
have modified the `Vm.java` file, this is what you will need to do to
submit the patch:

```
$ git add types/Vm.java
$ git commit -s
```

This will open an editor where you can write the commit message. By
default it will probaby be `vim`, but you can change it with the
`EDITOR` environment variable:

```
$ export EDITOR=my-favorite-editor
$ git add types/Vm.java
$ git commit -s
```

In that editor you will be asked to write a _commit message_. It is
important to write good commit messages, describing the reason for the
change. The first line should be a summary, then a blank line and your
description of the change. For example:

```
Improve the documentation of vm.start

This patch improves the documentation of the "vm.start"
operation, so that it is clear that the default value
of the "pause" parameter is "false".
```

Write the file, and you are ready to submit it:

```
$ git push origin HEAD:refs/for/master
```

If this finishes correctly it will give you the URL of the change. Go
there and make sure that there is at least a reviewer for your change.
In case of doubt add [Juan Hernández](maito:juan.hernandez@redhat.com)
as reviewer.

The reviewer may ask you to do changes to your patch, and will be happy
to assist you with any doubts you have with the tools.

Eventually your patch will be merged and will be part of the reference
documentation distributed with the next release of the software.

### Testing and previewing your changes

If your changes are simple enough there may be no need to test them,
just submit the patch. But if you are making larger changes you may want
to se how they will look like in the generated documentation. The first
step is to generate a `model.json` file containing the description of
the API. To do this you need to use _Maven_. Won't go into the details
of installing and using Maven here, as you can find plenty of resources
online and you will just need to run one simple command:

```bash
$ mvn validate -Pdescribe
```

This will analyze the model and create the `model.json` and `model.xml`
files inside the `target` directory. The important one is `model.json`:

```
$ find . -name model.json
target/model.json
```

This file contains a description of the model, including the
documentation. Take note of the location of this file, as it will be
needed later.

The second step is to install the documentation viewer. This is part
of a different `git` repository:

```
$ git clone https://gerrit.ovirt.org/ovirt-engine-api-explorer
```

This repository contains a _JavaScript_ application. To use it it needs
to be hosted in a web server. If you already have a running web server
you can put the application there. Or you can also use `npm`. For more
details see
[here](https://github.com/oVirt/ovirt-engine-api-explorer#using-locally).

Once this application is ready you can replace the `model.json` file
that it contains with the one that you built in the previous step,
reload the page and go to the relevant location. For example,
assuming that you are using `npm`:

```
$ cd ovirt-engine-api-exporer
$ cp .../model.json app/model.json
$ npm start
```

Then go with your browser to the relevant location:

http://localhost:8000/app/#/services/vm/methods/start

# Feedback/questions/issues

If you have any question, issue, or feedback please
contact [Juan Hernández](mailto:juan.hernandez@redhat.com).