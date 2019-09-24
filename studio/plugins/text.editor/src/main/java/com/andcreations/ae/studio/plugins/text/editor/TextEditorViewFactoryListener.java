package com.andcreations.ae.studio.plugins.text.editor;

/**
 * @author Mikolaj Gucki
 */
interface TextEditorViewFactoryListener {
    /** */
    void textEditorCreated(EditorMediator mediator,
        TextEditorViewHandler viewHandler);
}
