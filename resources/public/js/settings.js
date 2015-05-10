var puresec = puresec || {};

$(document).ready(function () {

    // map/unmap handler
    $(".handler").click(function() {
        var handler = puresec.handler(this);
        var detector = puresec.detector($(this).closest(".detector")[0]);

        handler.isMapped()
            ? handler.unmapDetector(detector.getId())
            : handler.mapDetector(detector.getId());
    });

    // delete handler
    $(".btn_remove_handler").click(function() {
        puresec.handler(this.closest("tr"))
            .remove();
    });

    // delete detector
    $(".btn_remove_detector").click(function() {
        puresec.detector(this.closest("tr"))
            .remove();
    });

});
