package rs._1._4.rs1124.service.process.checkout;

public class CheckoutTestCase {
    private String toolCode;
    private String checkoutDate;
    private String rentalDayCount;
    private String discountPercent;
    private boolean expectingFailure;
    private String[] expectations;
    private String comment;

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getRentalDayCount() {
        return rentalDayCount;
    }

    public void setRentalDayCount(String rentalDayCount) {
        this.rentalDayCount = rentalDayCount;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean isExpectingFailure() {
        return expectingFailure;
    }

    public void setExpectingFailure(boolean expectingFailure) {
        this.expectingFailure = expectingFailure;
    }

    public String[] getExpectations() {
        return expectations;
    }

    public void setExpectations(String[] expectations) {
        this.expectations = expectations;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
