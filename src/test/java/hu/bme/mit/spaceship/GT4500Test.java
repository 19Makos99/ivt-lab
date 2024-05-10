package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

// Added extra line for merge conflict

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimary;
  private TorpedoStore mockSecondary;

  @BeforeEach
  public void init(){
    mockPrimary = mock(TorpedoStore.class);
    mockSecondary = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimary, mockSecondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.getTorpedoCount()).thenReturn(1);
    when(mockPrimary.fire(anyInt())).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.getTorpedoCount()).thenReturn(1);
    when(mockSecondary.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(1)).getTorpedoCount();
    verify(mockPrimary, times(1)).fire(anyInt());
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(1)).getTorpedoCount();
    verify(mockSecondary, times(1)).fire(anyInt());
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiresFirst() {
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.getTorpedoCount()).thenReturn(1);
    when(mockPrimary.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(0)).getTorpedoCount();
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).isEmpty();
    verify(mockSecondary, times(0)).getTorpedoCount();
    verify(mockSecondary, times(0)).fire(anyInt());
  }

  @Test
  public void fireTorpedo_Single_PrimaryEmptySecondaryFires() {
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockPrimary.getTorpedoCount()).thenReturn(1);
    when(mockPrimary.fire(anyInt())).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.getTorpedoCount()).thenReturn(1);
    when(mockSecondary.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(0)).getTorpedoCount();
    verify(mockPrimary, times(0)).fire(anyInt());
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(0)).getTorpedoCount();
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryThenSecondaryFires() {
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.getTorpedoCount()).thenReturn(1);
    when(mockPrimary.fire(anyInt())).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.getTorpedoCount()).thenReturn(1);
    when(mockSecondary.fire(anyInt())).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(0)).getTorpedoCount();
    verify(mockPrimary, times(1)).fire(anyInt());
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(0)).getTorpedoCount();
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFailsSecondaryFires() {
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockPrimary.getTorpedoCount()).thenReturn(1);
    when(mockPrimary.fire(anyInt())).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.getTorpedoCount()).thenReturn(1);
    when(mockSecondary.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(0)).getTorpedoCount();
    verify(mockPrimary, times(0)).fire(anyInt());
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(0)).getTorpedoCount();
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiresDoubleWhenSecondaryIsEmpty() {
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.getTorpedoCount()).thenReturn(1);
    when(mockPrimary.fire(anyInt())).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(true);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result1);
    assertEquals(true, result2);
    verify(mockPrimary, times(2)).isEmpty();
    verify(mockPrimary, times(0)).getTorpedoCount();
    verify(mockPrimary, times(2)).fire(1);
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(0)).getTorpedoCount();
    verify(mockSecondary, times(0)).fire(anyInt());
  }

  @Test
  public void fireTorpedo_All_EveryTorpedoIsFired() {
    // Arrange
    var primaryTorpedoCount = 10;
    var secondaryTorpedoCout = 20;
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.getTorpedoCount()).thenReturn(primaryTorpedoCount);
    when(mockPrimary.fire(anyInt())).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.getTorpedoCount()).thenReturn(secondaryTorpedoCout);
    when(mockSecondary.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(1)).getTorpedoCount();
    verify(mockPrimary, times(1)).fire(primaryTorpedoCount);
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(1)).getTorpedoCount();
    verify(mockSecondary, times(1)).fire(secondaryTorpedoCout);
  }

  @Test
  public void fireTorpedo_All_EveryTorpedoStoreIsEmpty() {
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(0)).getTorpedoCount();
    verify(mockPrimary, times(0)).fire(anyInt());
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(0)).getTorpedoCount();
    verify(mockSecondary, times(0)).fire(anyInt());
  }
}
