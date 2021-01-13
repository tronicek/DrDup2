package p132;

public class A {

    public void actionPerformed(ActionEvent ae) {
        TimeRange timeRange = RBNBHelper.getChannelsTimeRange();
        double time = DateTimeDialog.showDialog(frame, rbnb.getLocation(), timeRange.start, timeRange.end);
        if (time >= 0) {
            rbnb.setLocation(time);
        }
    }
}
