var puresec = puresec || {};

$(document).ready(function () {

    // map trigger
    $(".trigger").click(function() {
        puresec.trigger(this).mapDetector(puresec.detector($(this).closest(".detector")[0]).getId());
    });

});
