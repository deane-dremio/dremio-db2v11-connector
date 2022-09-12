package com.dremio.exec.store.jdbc.dialect;

import com.dremio.exec.store.jdbc.dialect.arp.ArpYaml;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.SqlSelectOperator;
import org.apache.calcite.sql.SqlSelectKeyword;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import com.dremio.exec.store.jdbc.dialect.arp.ArpDialect;

import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlIntervalLiteral;
import org.apache.calcite.sql.SqlIntervalQualifier;

/**
 * A <code>SqlDialect</code> implementation for the IBM DB2 database.
 */
public class DB2Dialect extends ArpDialect {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DB2Dialect.class);
  
  /** Creates a DB2Dialect */
  public DB2Dialect(ArpYaml yaml) {
    super(yaml);
  }

  @Override public boolean supportsCharSet() {
    return false;
  }

  @Override public boolean hasImplicitTableAlias() {
    return false;
  }

  /*@Override public void unparseSqlIntervalQualifier(SqlWriter writer,
      SqlIntervalQualifier qualifier, RelDataTypeSystem typeSystem) {

    // DB2 supported qualifiers. Singular form of these keywords are also acceptable.
    // YEAR/YEARS
    // MONTH/MONTHS
    // DAY/DAYS
    // HOUR/HOURS
    // MINUTE/MINUTES
    // SECOND/SECONDS

    switch (qualifier.timeUnitRange) {
    case YEAR:
    case MONTH:
    case DAY:
    case HOUR:
    case MINUTE:
    case SECOND:
    case MICROSECOND:
      final String timeUnit = qualifier.timeUnitRange.startUnit.name();
      writer.keyword(timeUnit);
      break;
    default:
      throw new AssertionError("Unsupported type: " + qualifier.timeUnitRange);
    }

    if (null != qualifier.timeUnitRange.endUnit) {
      throw new AssertionError("Unsupported end unit: "
          + qualifier.timeUnitRange.endUnit);
    }
  }

  @Override public void unparseSqlIntervalLiteral(SqlWriter writer,
      SqlIntervalLiteral literal, int leftPrec, int rightPrec) {
    // A duration is a positive or negative number representing an interval of time.
    // If one operand is a date, the other labeled duration of YEARS, MONTHS, or DAYS.
    // If one operand is a time, the other must be labeled duration of HOURS, MINUTES, or SECONDS.
    // If one operand is a timestamp, the other operand can be any of teh duration.

    SqlIntervalLiteral.IntervalValue interval =
        (SqlIntervalLiteral.IntervalValue) literal.getValue();
    if (interval.getSign() == -1) {
      writer.print("-");
    }
    writer.literal(literal.getValue().toString());
    unparseSqlIntervalQualifier(writer, interval.getIntervalQualifier(),
        RelDataTypeSystem.DEFAULT);
  }*/

}

// End DB2Dialect.java