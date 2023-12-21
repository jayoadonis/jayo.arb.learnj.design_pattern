package jayo.arb.learnj.design_pattern_lib.creational.builder.fruit_product;

public enum Value {

    UNKNOWN( (byte)0b0000_0000, "UNK", "UNKNOWN" ),
    WAITING( (byte)0b0000_0001, "WTG", "WAITING" );

    private Value(
            final byte CODE,
            final String VALUE,
            final String DESCRIPTION
    ) {
        this.CODE = CODE;
        this.VALUE = VALUE;
        this.DESCRIPTION = DESCRIPTION;
    }

    public final byte CODE;
    public final String VALUE;
    public final String DESCRIPTION;
}
