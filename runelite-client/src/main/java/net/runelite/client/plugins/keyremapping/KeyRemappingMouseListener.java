/*
 * Copyright (c) 2025, RuneLite Contributors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.keyremapping;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseListener;

class KeyRemappingMouseListener implements MouseListener
{
    private static final int MOUSE_BUTTON_4 = 4;
    private static final int MOUSE_BUTTON_5 = 5;
    private static final int[] MODIFIER_MASKS = {
        java.awt.event.InputEvent.CTRL_DOWN_MASK,
        java.awt.event.InputEvent.ALT_DOWN_MASK,
        java.awt.event.InputEvent.SHIFT_DOWN_MASK,
        java.awt.event.InputEvent.META_DOWN_MASK
    };
    private static final int[] MODIFIER_VK = {
        KeyEvent.VK_CONTROL,
        KeyEvent.VK_ALT,
        KeyEvent.VK_SHIFT,
        KeyEvent.VK_META
    };

    @Inject
    private Client client;

    @Inject
    private KeyManager keyManager;

    @Inject
    private KeyRemappingPlugin plugin;

    @Inject
    private KeyRemappingConfig config;

    private void synthesizeKey(int keyEventId, int keyCode) throws AWTException {
        // Dispatch a synthetic key event to the key manager so existing
        // key listeners (including KeyRemappingListener and the client) handle it

        Robot r = new Robot();
        r.keyPress(keyCode);
        r.keyRelease(keyCode);
    }

    private boolean handleExtraButton(int button) throws AWTException {
        // Only act when chatbox is focused; follow plugin rules
        if (!plugin.chatboxFocused())
        {
            return false;
        }

        // Prefer generic keybinds when set
        if (button == MOUSE_BUTTON_4)
        {
            return dispatchKeybind(config.mouse4Keybind());
        }

        if (button == MOUSE_BUTTON_5)
        {
            return dispatchKeybind(config.mouse5Keybind());
        }

        return false;
    }

    private boolean dispatchKeybind(net.runelite.client.config.Keybind keybind) throws AWTException {
        if (keybind == null || net.runelite.client.config.Keybind.NOT_SET.equals(keybind))
        {
            return false;
        }

        int keyCode = keybind.getKeyCode();
        if (keyCode != KeyEvent.VK_UNDEFINED)
        {
            synthesizeKey(KeyEvent.KEY_PRESSED, keyCode);
            synthesizeKey(KeyEvent.KEY_RELEASED, keyCode);
        }

        return true;
    }

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mousePressed(MouseEvent mouseEvent)
    {
        int button = mouseEvent.getButton();
        if (button == MOUSE_BUTTON_4 || button == MOUSE_BUTTON_5)
        {
            try {
                if (handleExtraButton(button))
                {
                    mouseEvent.consume();
                }
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        }
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseEntered(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseExited(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseDragged(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseMoved(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }
}
