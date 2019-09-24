Plugin XCode project settings
====

1. Create XCode project ios/project in the plugin directory. Name it ae_PLUGIN where PLUGIN is the plugin name.
2. Build settings
    1. HEADER_SEARCH_PATH: $(AE_DIST)/src/lua/include $(AE_DIST)/src/ae/include
    $(PLUGIN_DIR)/common/src/cpp/include $(PLUGIN_DIR)/ios/src/include
    2. Other plugin specific HEADER_SEARCH_PATH
    3. User-defined values (Editor -> Add Build Settings -> Add User-Defined Setting)
        1. AE_DIST: point the AE distribution directory
        2. PLUGIN_DIR: $(PROJECT_DIR)/../../..
3. Create directory (group) inside the project for the sources of the XCode project name (e.g. ae_gles2). Create subdirectories: ios/src, common/src.