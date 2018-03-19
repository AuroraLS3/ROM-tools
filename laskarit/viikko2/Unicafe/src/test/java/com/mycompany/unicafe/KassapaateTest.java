package com.mycompany.unicafe;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KassapaateTest {

    private Kassapaate kassapaate;

    @Before
    public void setUp() {
        kassapaate = new Kassapaate();
    }

    @Test
    public void luotuOikein() {
        assertEquals(100000, kassapaate.kassassaRahaa());
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void kateinenToimiiMaukas() {
        assertEquals(40, kassapaate.syoMaukkaasti(440));
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void kateinenToimiiEdullinen() {
        assertEquals(60, kassapaate.syoEdullisesti(300));
        assertEquals(1, kassapaate.edullisiaLounaitaMyyty());
    }

    @Test
    public void kateinenEiTarpeeksiEdullinen() {
        assertEquals(200, kassapaate.syoEdullisesti(200));
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }

    @Test
    public void kateinenEiTarpeeksiMaukas() {
        assertEquals(200, kassapaate.syoMaukkaasti(200));
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void korttiSyoEdullisesti() {
        Maksukortti kortti = new Maksukortti(300);
        assertTrue(kassapaate.syoEdullisesti(kortti));
        assertEquals(1, kassapaate.edullisiaLounaitaMyyty());
        assertEquals(100000, kassapaate.kassassaRahaa());
        assertEquals(60, kortti.saldo());
    }

    @Test
    public void korttiSyoMaukkaasti() {
        Maksukortti kortti = new Maksukortti(500);
        assertTrue(kassapaate.syoMaukkaasti(kortti));
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
        assertEquals(100000, kassapaate.kassassaRahaa());
        assertEquals(100, kortti.saldo());
    }

    @Test
    public void korttiEiTarpeeksiEdullinen() {
        Maksukortti kortti = new Maksukortti(200);
        assertFalse(kassapaate.syoEdullisesti(kortti));
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
        assertEquals(100000, kassapaate.kassassaRahaa());
        assertEquals(200, kortti.saldo());
    }

    @Test
    public void korttiEiTarpeeksiMaukas() {
        Maksukortti kortti = new Maksukortti(300);
        assertFalse(kassapaate.syoMaukkaasti(kortti));
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
        assertEquals(100000, kassapaate.kassassaRahaa());
        assertEquals(300, kortti.saldo());
    }

    @Test
    public void kortinLatausToimii() {
        Maksukortti kortti = new Maksukortti(0);
        kassapaate.lataaRahaaKortille(kortti, 100);
        assertEquals(100100, kassapaate.kassassaRahaa());
        assertEquals(100, kortti.saldo());
    }

    @Test
    public void kortinLatausToimiiNegatiivisella() {
        Maksukortti kortti = new Maksukortti(0);
        kassapaate.lataaRahaaKortille(kortti, -100);
        assertEquals(100000, kassapaate.kassassaRahaa());
        assertEquals(0, kortti.saldo());
        kortti.toString();
        kassapaate.toString();
    }
}