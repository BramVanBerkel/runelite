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
import java.awt.event.MouseEvent;
import net.runelite.api.Client;
import net.runelite.client.config.Keybind;
import net.runelite.client.input.KeyManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KeyRemappingMouseListenerTest
{
    private KeyRemappingMouseListener mouseListener;

    @Mock
    private Client client;

    @Mock
    private KeyManager keyManager;

    @Mock
    private KeyRemappingPlugin plugin;

    @Mock
    private KeyRemappingConfig config;

    @Before
    public void setup()
    {
        mouseListener = new KeyRemappingMouseListener();
        inject(mouseListener, "client", client);
        inject(mouseListener, "keyManager", keyManager);
        inject(mouseListener, "plugin", plugin);
        inject(mouseListener, "config", config);
    }

    private static void inject(Object target, String fieldName, Object value)
    {
        try
        {
            java.lang.reflect.Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNoConsumeWhenChatNotFocused()
    {
        when(plugin.chatboxFocused()).thenReturn(false);

        MouseEvent e = mock(MouseEvent.class);
        when(e.getButton()).thenReturn(4);

        mouseListener.mousePressed(e);

        // Should not consume when chatbox isn't focused
        verify(e, never()).consume();
    }

    @Test
    public void testMouse4ConsumesWhenKeybindSet()
    {
        when(plugin.chatboxFocused()).thenReturn(true);
        when(config.mouse4Keybind()).thenReturn(Keybind.SHIFT);

        MouseEvent e = mock(MouseEvent.class);
        when(e.getButton()).thenReturn(4);

        mouseListener.mousePressed(e);

        verify(e).consume();
    }

    @Test
    public void testMouse5ConsumesWhenKeybindSet()
    {
        when(plugin.chatboxFocused()).thenReturn(true);
        when(config.mouse5Keybind()).thenReturn(Keybind.SHIFT);

        MouseEvent e = mock(MouseEvent.class);
        when(e.getButton()).thenReturn(5);

        mouseListener.mousePressed(e);

        verify(e).consume();
    }

    @Test
    public void testNoConsumeWhenKeybindNotSet()
    {
        when(plugin.chatboxFocused()).thenReturn(true);
        when(config.mouse4Keybind()).thenReturn(Keybind.NOT_SET);

        MouseEvent e = mock(MouseEvent.class);
        when(e.getButton()).thenReturn(4);

        mouseListener.mousePressed(e);

        verify(e, never()).consume();
    }
}
