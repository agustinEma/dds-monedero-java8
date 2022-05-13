package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner() {

    cuenta.poner(1500);
    assertEquals(1500, cuenta.getSaldo());
  }

  @Test
  void PonerMontoNegativo() {
    MontoNegativoException thrown = assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
    Assertions.assertEquals("-1500.0: el monto a ingresar debe ser un valor positivo", thrown.getMessage());
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(3856, cuenta.getSaldo());
  }

  @Test
  void MasDeTresDepositos() {
    MaximaCantidadDepositosException thrown = assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
    Assertions.assertEquals("Ya excedio los 3 depositos diarios", thrown.getMessage());
  }

  @Test
  void ExtraerMasQueElSaldo() {
    SaldoMenorException thrown = assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
    Assertions.assertEquals("No puede sacar mas de " + cuenta.getSaldo() + " $", thrown.getMessage());
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {

    MontoNegativoException thrown = assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
    Assertions.assertEquals("-500.0: el monto a ingresar debe ser un valor positivo", thrown.getMessage());
  }

}