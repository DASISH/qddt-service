package no.nsd.qddt.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleMyException(Exception  exception) {
//        return new ModelAndView("redirect:errorMessage?error="+exception.getMessage(),);
//    }
//
//    @RequestMapping(value="/errorMessage", method= RequestMethod.GET)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public String handleMyExceptionOnRedirect(@RequestParam("error") String error) {
//        return error;
//    }

}
