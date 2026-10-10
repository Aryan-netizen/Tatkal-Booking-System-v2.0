package io.bootify.my_app.payment;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the gatewayRef value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = PaymentGatewayRefUnique.PaymentGatewayRefUniqueValidator.class
)
public @interface PaymentGatewayRefUnique {

    String message() default "{Exists.payment.gatewayRef}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PaymentGatewayRefUniqueValidator implements ConstraintValidator<PaymentGatewayRefUnique, String> {

        private final PaymentService paymentService;
        private final HttpServletRequest request;

        public PaymentGatewayRefUniqueValidator(final PaymentService paymentService,
                final HttpServletRequest request) {
            this.paymentService = paymentService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(paymentService.get(currentId).getGatewayRef())) {
                // value hasn't changed
                return true;
            }
            return !paymentService.gatewayRefExists(value);
        }

    }

}
