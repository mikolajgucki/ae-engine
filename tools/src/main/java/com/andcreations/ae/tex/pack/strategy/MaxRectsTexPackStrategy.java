package com.andcreations.ae.tex.pack.strategy;

import java.util.List;

import com.andcreations.ae.tex.pack.TexPackException;
import com.badlogic.gdx.tools.texturepacker.MaxRectsPacker;
import com.badlogic.gdx.tools.texturepacker.PubRect;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Page;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Rect;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.Array;

/**
 * @author Mikolaj Gucki
 */
public class MaxRectsTexPackStrategy extends AbstractTexPackStrategy {
    /** */
    @Override
    public void pack(List<Subtexture> subtextures)
        throws TexPackException,SubtexturesDontFitException {
    // settings        
        Settings settings = new Settings();
        settings.paddingX = 0;
        settings.paddingY = 0;
        settings.rotation = false;
        settings.silent = true;
        
    // width
        settings.minWidth = getWidth();
        settings.maxWidth = settings.minWidth;
        
    // height
        settings.minHeight = getHeight();
        settings.maxHeight = settings.minHeight;
        
    // rectangles
        Array<Rect> rects = new Array<Rect>();
        for (Subtexture subtexture:subtextures) {
            Rect rect = new PubRect();
            rects.add(rect);
            
            rect.name = subtexture.getId();
            rect.width = subtexture.getWidth();
            rect.height = subtexture.getHeight();
        }
        
    // pack
        Array<Page> pages;
        try {
            MaxRectsPacker packer = new MaxRectsPacker(settings);
            pages = packer.pack(rects);
        } catch (RuntimeException exception) {
            String msg = exception.getMessage();
            if (msg.startsWith("Image does not fit")) {
                throw new SubtexturesDontFitException();
            }
            throw new TexPackException(exception.getMessage(),exception);
        }
        
        if (pages.size != 1) {
            throw new SubtexturesDontFitException();
        }
        
        Page page = pages.first();
    // set locations
        for (Subtexture subtexture:subtextures) {
            Rect subtextureRect = null;
            for (Rect rect:page.outputRects) {
                if (rect.name.equals(subtexture.getId())) {
                    subtextureRect = rect;
                    break;
                }
            }
            
            subtexture.setLocation(subtextureRect.x,subtextureRect.y);
        }
    }
}