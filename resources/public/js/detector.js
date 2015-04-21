var puresec = puresec || {};

puresec.detector = function(detector) {
    var $detector = $(detector);
    var detectorId = $detector.data("detector-id");

    var getId = function() {
        return detectorId;
    };

    return {
        "getId": getId
    };
};
