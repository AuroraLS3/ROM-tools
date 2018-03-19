package com.mycompany.unicafe;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti != null);
    }

    @Test
    public void alkuSaldoOikein() {
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void lataaSaldoa() {
        kortti.lataaRahaa(10);
        assertEquals(20, kortti.saldo());
    }

    @Test
    public void eiLataaNegatiivistaSaldoa() {
        kortti.lataaRahaa(-10);
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void vahenee() {
        assertTrue(kortti.otaRahaa(5));
        assertEquals(5, kortti.saldo());
    }

    @Test
    public void eiVaheneVaarin() {
        assertFalse(kortti.otaRahaa(11));
        assertEquals(10, kortti.saldo());
    }


}
