package jp.gihyo.shoppingapp.flux

abstract class ActionCreator<Action>(val dispatcher: Dispatcher<Action>)