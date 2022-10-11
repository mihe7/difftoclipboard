# Diff to Clipboard

## Description and usage

Experimental code for a NetBeans Plugin that generates and displays a diff between selected Java text and the system's clipboard.

1. Copy some text to the clipboard
2. Select text in some Java file within NetBeans
3. From the context menu in the Java editor select "Diff to clipboard"

If everything worked, the diff will be displayed in a new tab.

## Requirements

NetBeans 12.2 and higher. Tested with NetBeans 15.

## Build instructions

A simple

```sh
mvn clean package
```

should be enough. You'll find the NBM-file in the `target`-folder.

**Warning**: code is probably of low quality, intended for experimental purposes only. Use at your own risk.

