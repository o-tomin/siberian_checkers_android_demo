package com.checkers.entity;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

public class KillersContractTest {

    private KillersContract contract;
    private Cell cellMock;
    private List<Cell> victimsMock;

    @BeforeMethod
    public void init() {
        this.contract = new KillersContract();
        this.cellMock = mock(Cell.class);
        this.victimsMock = mock(ArrayList.class);
    }

    @Test
    public void addGetKillerTest() {
        contract = contract.addKiller(cellMock)
                .addVictims(victimsMock).sign();

        assertEquals(contract.getKillers().iterator().next(), cellMock);
    }

    @Test
    public void addGetVictimsTest() {
        contract = contract.addKiller(cellMock)
                .addVictims(victimsMock).sign();
        Cell killer = contract.getKillers().iterator().next();

        assertEquals(contract.getVictims(killer), victimsMock);
    }

    @Test
    public void signTest() {
        assertFalse(contract.isValid());

        contract = contract.addKiller(cellMock)
                .addVictims(victimsMock).sign();

        contract.sign();

        assertTrue(contract.isValid());
    }
}
