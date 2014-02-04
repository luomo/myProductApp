package controllers

import play.api.mvc.{Flash, Action, Controller}
import model.Product
import play.api.data.Form
import play.api.data.Forms.{mapping, longNumber, nonEmptyText}
import play.api.i18n.Messages
import play.api.data.validation.{Invalid, Valid, ValidationError, Constraint}
import play.api.Logger
import play.api.Play.current

/**
 * User: joao.cunha
 */
object Products extends Controller{

  def list = Action {
    implicit request =>
      log(current.configuration.getString("logger.root"))
      val products = Product.findAll
      Ok(views.html.products.list(products))
  }

  def log(toLog : Object ) = Logger.info(toLog.toString)

  def show(ean: Long) = Action { implicit request =>
    Product.findByEan(ean).map { product =>
      Ok(views.html.products.details(product))
    }.getOrElse(NotFound)
  }

  def save = Action { implicit request =>
    val newProductForm = productForm.bindFromRequest()
    newProductForm.fold(
      hasErrors = { form =>
        Redirect(routes.Products.newProduct()).
          flashing(Flash(form.data) + ("error" -> Messages("validation.errors")))
      },
      success = { newProduct =>
        Product.add(newProduct)
        val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.Products.show(newProduct.ean)).flashing("success" -> message)
      }
    )
  }


  def newProduct = Action { implicit request =>
    val form = if (flash.get("error").isDefined)
      productForm.bind(flash.data)
    else
      productForm
    Ok(views.html.products.editProduct(form))
  }

 private val productForm: Form[model.Product] = Form(
    mapping(
      "id" -> longNumber,
      "ean" -> longNumber.verifying("validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )


 /* private val productForm: Form[model.Product] = Form(
    mapping(
      "ean" -> longNumber.verifying("validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
      verifying("Failed form contraints", fields => fields match {case prod => validate(prod).isDefined})
  )


  val passwordCheckConstraint: Constraint[model.Product] = Constraint("constraints.passwordcheck")({
    prod =>
      val errors = prod.ean match {
        case ean if(prod.toString.size == 11 && prod.toString.size == 12) &&
          longNumber.verifying("validation.ean.duplicate", Product.findByEan(_).isEmpty) => Nil
        case _ => Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })*/

}
