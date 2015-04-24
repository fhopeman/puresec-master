var puresec = puresec || {};

$(document).ready(function () {

    // map/unmap trigger
    $(".trigger").click(function() {
        var trigger = puresec.trigger(this);
        var detector = puresec.detector($(this).closest(".detector")[0]);

        trigger.isMapped()
            ? trigger.unmapDetector(detector.getId())
            : trigger.mapDetector(detector.getId());
    });

});
